/*
 Author:       Mike Adair madairATdmsolutions.ca
 Richard Greenwood rich@greenwoodmap.com
 License:      MIT as per: ../LICENSE

 $Id: Proj.js 2956 2007-07-09 12:17:52Z steven $
 */

/**
 * Namespace: Proj4js
 *
 * Proj4js is a JavaScript library to transform point coordinates from one
 * coordinate system to another, including datum transformations.
 *
 * This library is a port of both the Proj.4 and GCTCP C libraries to JavaScript.
 * Enabling these transformations in the browser allows geographic data stored
 * in different projections to be combined in browser-based web mapping
 * applications.
 *
 * Proj4js must have access to coordinate system initialization strings (which
 * are the same as for PROJ.4 command line).  Thes can be included in your
 * application using a <script> tag or Proj4js can load CS initialization
 * strings from a local directory or a web service such as spatialreference.org.
 *
 * Similarly, Proj4js must have access to projection transform code.  These can
 * be included individually using a <script> tag in your page, built into a
 * custom build of Proj4js or loaded dynamically at run-time.  Using the
 * -combined and -compressed versions of Proj4js includes all projection class
 * code by default.
 *
 * Note that dynamic loading of defs and code happens ascynchrously, check the
 * Proj.readyToUse flag before using the Proj object.  If the defs and code
 * required by your application are loaded through script tags, dynamic loading
 * is not required and the Proj object will be readyToUse on return from the
 * constructor.
 *
 * All coordinates are handled as points which have a .x and a .y property
 * which will be modified in place.
 *
 * Override Proj4js.reportError for output of alerts and warnings.
 *
 * See http://trac.osgeo.org/proj4js/wiki/UserGuide for full details.
 */

/**
 * Global namespace object for Proj4js library
 */
var Proj4js = {
    /**
     * Property: defaultDatum
     * The datum to use when no others a specified
     */
    defaultDatum: 'WGS84', //default datum

    /**
     * Method: transform(source, dest, point)
     * Transform a point coordinate from one map projection to another.  This is
     * really the only public method you should need to use.
     *
     * Parameters:
     * source - {Proj4js.Proj} source map projection for the transformation
     * dest - {Proj4js.Proj} destination map projection for the transformation
     * point - {Object} point to transform, may be geodetic (long, lat) or
     *     projected Cartesian (x,y), but should always have x,y properties.
     */
    transform: function(source, dest, point) {
        if (!source.readyToUse) {
            this.reportError("Proj4js initialization for:" + source.srsCode + " not yet complete");
            return point;
        }
        if (!dest.readyToUse) {
            this.reportError("Proj4js initialization for:" + dest.srsCode + " not yet complete");
            return point;
        }

        // Workaround for datum shifts towgs84, if either source or destination projection is not wgs84
        if (source.datum && dest.datum && (
                ((source.datum.datum_type == Proj4js.common.PJD_3PARAM || source.datum.datum_type == Proj4js.common.PJD_7PARAM) && dest.datumCode != "WGS84") ||
                ((dest.datum.datum_type == Proj4js.common.PJD_3PARAM || dest.datum.datum_type == Proj4js.common.PJD_7PARAM) && source.datumCode != "WGS84"))) {
            var wgs84 = Proj4js.WGS84;
            this.transform(source, wgs84, point);
            source = wgs84;
        }

        // DGR, 2010/11/12
        if (source.axis != "enu") {
            this.adjust_axis(source, false, point);
        }

        // Transform source points to long/lat, if they aren't already.
        if (source.projName == "longlat") {
            point.x *= Proj4js.common.D2R;  // convert degrees to radians
            point.y *= Proj4js.common.D2R;
        } else {
            if (source.to_meter) {
                point.x *= source.to_meter;
                point.y *= source.to_meter;
            }
            source.inverse(point); // Convert Cartesian to longlat
        }

        // Adjust for the prime meridian if necessary
        if (source.from_greenwich) {
            point.x += source.from_greenwich;
        }

        // Convert datums if needed, and if possible.
        point = this.datum_transform(source.datum, dest.datum, point);

        // Adjust for the prime meridian if necessary
        if (dest.from_greenwich) {
            point.x -= dest.from_greenwich;
        }

        if (dest.projName == "longlat") {
            // convert radians to decimal degrees
            point.x *= Proj4js.common.R2D;
            point.y *= Proj4js.common.R2D;
        } else {               // else project
            dest.forward(point);
            if (dest.to_meter) {
                point.x /= dest.to_meter;
                point.y /= dest.to_meter;
            }
        }

        // DGR, 2010/11/12
        if (dest.axis != "enu") {
            this.adjust_axis(dest, true, point);
        }

        return point;
    }, // transform()

    /** datum_transform()
     source coordinate system definition,
     destination coordinate system definition,
     point to transform in geodetic coordinates (long, lat, height)
     */
    datum_transform: function(source, dest, point) {

        // Short cut if the datums are identical.
        if (source.compare_datums(dest)) {
            return point; // in this case, zero is sucess,
            // whereas cs_compare_datums returns 1 to indicate TRUE
            // confusing, should fix this
        }

        // Explicitly skip datum transform by setting 'datum=none' as parameter for either source or dest
        if (source.datum_type == Proj4js.common.PJD_NODATUM
                || dest.datum_type == Proj4js.common.PJD_NODATUM) {
            return point;
        }

        //DGR: 2012-07-29 : add nadgrids support (begin)
        var src_a = source.a;
        var src_es = source.es;

        var dst_a = dest.a;
        var dst_es = dest.es;

        var fallback = source.datum_type;
        // If this datum requires grid shifts, then apply it to geodetic coordinates.
        if (fallback == Proj4js.common.PJD_GRIDSHIFT)
        {
            if (this.apply_gridshift(source, 0, point) == 0) {
                source.a = Proj4js.common.SRS_WGS84_SEMIMAJOR;
                source.es = Proj4js.common.SRS_WGS84_ESQUARED;
            } else {

                // try 3 or 7 params transformation or nothing ?
                if (!source.datum_params) {
                    source.a = src_a;
                    source.es = source.es;
                    return point;
                }
                var wp = 1.0;
                for (var i = 0, l = source.datum_params.length; i < l; i++) {
                    wp *= source.datum_params[i];
                }
                if (wp == 0.0) {
                    source.a = src_a;
                    source.es = source.es;
                    return point;
                }
                fallback = source.datum_params.length > 3 ?
                        Proj4js.common.PJD_7PARAM
                        : Proj4js.common.PJD_3PARAM;
                // CHECK_RETURN;
            }
        }

        if (dest.datum_type == Proj4js.common.PJD_GRIDSHIFT)
        {
            dest.a = Proj4js.common.SRS_WGS84_SEMIMAJOR;
            dest.es = Proj4js.common.SRS_WGS84_ESQUARED;
        }
        // Do we need to go through geocentric coordinates?
        if (source.es != dest.es || source.a != dest.a
                || fallback == Proj4js.common.PJD_3PARAM
                || fallback == Proj4js.common.PJD_7PARAM
                || dest.datum_type == Proj4js.common.PJD_3PARAM
                || dest.datum_type == Proj4js.common.PJD_7PARAM)
        {
            //DGR: 2012-07-29 : add nadgrids support (end)

            // Convert to geocentric coordinates.
            source.geodetic_to_geocentric(point);
            // CHECK_RETURN;

            // Convert between datums
            if (source.datum_type == Proj4js.common.PJD_3PARAM || source.datum_type == Proj4js.common.PJD_7PARAM) {
                source.geocentric_to_wgs84(point);
                // CHECK_RETURN;
            }

            if (dest.datum_type == Proj4js.common.PJD_3PARAM || dest.datum_type == Proj4js.common.PJD_7PARAM) {
                dest.geocentric_from_wgs84(point);
                // CHECK_RETURN;
            }

            // Convert back to geodetic coordinates
            dest.geocentric_to_geodetic(point);
            // CHECK_RETURN;
        }

        // Apply grid shift to destination if required
        if (dest.datum_type == Proj4js.common.PJD_GRIDSHIFT)
        {
            this.apply_gridshift(dest, 1, point);
            // CHECK_RETURN;
        }

        source.a = src_a;
        source.es = src_es;
        dest.a = dst_a;
        dest.es = dst_es;

        return point;
    }, // cs_datum_transform

    /**
     * This is the real workhorse, given a gridlist
     * DGR: 2012-07-29 addition based on proj4 trunk
     */
    apply_gridshift: function(srs, inverse, point) {
        if (srs.grids == null || srs.grids.length == 0) {
            return -38;
        }
        var input = {"x": point.x, "y": point.y};
        var output = {"x": Number.NaN, "y": Number.NaN};
        /* keep trying till we find a table that works */
        var onlyMandatoryGrids = false;
        for (var i = 0, l = srs.grids.length; i < l; i++) {
            var gi = srs.grids[i];
            onlyMandatoryGrids = gi.mandatory;
            var ct = gi.grid;
            if (ct == null) {
                if (gi.mandatory) {
                    this.reportError("unable to find '" + gi.name + "' grid.");
                    return -48;
                }
                continue;//optional grid
            }
            /* skip tables that don't match our point at all.  */
            var epsilon = (Math.abs(ct.del[1]) + Math.abs(ct.del[0])) / 10000.0;
            if (ct.ll[1] - epsilon > input.y || ct.ll[0] - epsilon > input.x ||
                    ct.ll[1] + (ct.lim[1] - 1) * ct.del[1] + epsilon < input.y ||
                    ct.ll[0] + (ct.lim[0] - 1) * ct.del[0] + epsilon < input.x) {
                continue;
            }
            /* If we have child nodes, check to see if any of them apply. */
            /* TODO : only plain grid has been implemented ... */
            /* we found a more refined child node to use */
            /* load the grid shift info if we don't have it. */
            /* TODO : Proj4js.grids pre-loaded (as they can be huge ...) */
            /* skip numerical computing error when "null" grid (identity grid): */
            if (gi.name == "null") {
                output.x = input.x;
                output.y = input.y;
            } else {
                output = Proj4js.common.nad_cvt(input, inverse, ct);
            }
            if (!isNaN(output.x)) {
                break;
            }
        }
        if (isNaN(output.x)) {
            if (!onlyMandatoryGrids) {
                this.reportError("failed to find a grid shift table for location '" +
                        input.x * Proj4js.common.R2D + " " + input.y * Proj4js.common.R2D +
                        " tried: '" + srs.nadgrids + "'");
                return -48;
            }
            return -1;//FIXME: no shift applied ...
        }
        point.x = output.x;
        point.y = output.y;
        return 0;
    },
    /**
     * Function: adjust_axis
     * Normalize or de-normalized the x/y/z axes.  The normal form is "enu"
     * (easting, northing, up).
     * Parameters:
     * crs {Proj4js.Proj} the coordinate reference system
     * denorm {Boolean} when false, normalize
     * point {Object} the coordinates to adjust
     */
    adjust_axis: function(crs, denorm, point) {
        var xin = point.x, yin = point.y, zin = point.z || 0.0;
        var v, t;
        for (var i = 0; i < 3; i++) {
            if (denorm && i == 2 && point.z === undefined) {
                continue;
            }
            if (i == 0) {
                v = xin;
                t = 'x';
            }
            else if (i == 1) {
                v = yin;
                t = 'y';
            }
            else {
                v = zin;
                t = 'z';
            }
            switch (crs.axis[i]) {
                case 'e':
                    point[t] = v;
                    break;
                case 'w':
                    point[t] = -v;
                    break;
                case 'n':
                    point[t] = v;
                    break;
                case 's':
                    point[t] = -v;
                    break;
                case 'u':
                    if (point[t] !== undefined) {
                        point.z = v;
                    }
                    break;
                case 'd':
                    if (point[t] !== undefined) {
                        point.z = -v;
                    }
                    break;
                default :
                    alert("ERROR: unknow axis (" + crs.axis[i] + ") - check definition of " + crs.projName);
                    return null;
            }
        }
        return point;
    },
    /**
     * Function: reportError
     * An internal method to report errors back to user.
     * Override this in applications to report error messages or throw exceptions.
     */
    reportError: function(msg) {
        if (console)
            console.log(msg);
    },
    /**
     *
     * Title: Private Methods
     * The following properties and methods are intended for internal use only.
     *
     * This is a minimal implementation of JavaScript inheritance methods so that
     * Proj4js can be used as a stand-alone library.
     * These are copies of the equivalent OpenLayers methods at v2.7
     */

    /**
     * Function: extend
     * Copy all properties of a source object to a destination object.  Modifies
     *     the passed in destination object.  Any properties on the source object
     *     that are set to undefined will not be (re)set on the destination object.
     *
     * Parameters:
     * destination - {Object} The object that will be modified
     * source - {Object} The object with properties to be set on the destination
     *
     * Returns:
     * {Object} The destination object.
     */
    extend: function(destination, source) {
        destination = destination || {};
        if (source) {
            for (var property in source) {
                var value = source[property];
                if (value !== undefined) {
                    destination[property] = value;
                }
            }
        }
        return destination;
    },
    /**
     * Constructor: Class
     * Base class used to construct all other classes. Includes support for
     *     multiple inheritance.
     *
     */
    Class: function() {
        var Class = function() {
            this.initialize.apply(this, arguments);
        };

        var extended = {};
        var parent;
        for (var i = 0; i < arguments.length; ++i) {
            if (typeof arguments[i] == "function") {
                // get the prototype of the superclass
                parent = arguments[i].prototype;
            } else {
                // in this case we're extending with the prototype
                parent = arguments[i];
            }
            Proj4js.extend(extended, parent);
        }
        Class.prototype = extended;

        return Class;
    },
    /**
     * Function: bind
     * Bind a function to an object.  Method to easily create closures with
     *     'this' altered.
     *
     * Parameters:
     * func - {Function} Input function.
     * object - {Object} The object to bind to the input function (as this).
     *
     * Returns:
     * {Function} A closure with 'this' set to the passed in object.
     */
    bind: function(func, object) {
        // create a reference to all arguments past the second one
        var args = Array.prototype.slice.apply(arguments, [2]);
        return function() {
            // Push on any additional arguments from the actual function call.
            // These will come after those sent to the bind call.
            var newArgs = args.concat(
                    Array.prototype.slice.apply(arguments, [0])
                    );
            return func.apply(object, newArgs);
        };
    },
    /**
     * The following properties and methods handle dynamic loading of JSON objects.
     */

    /**
     * Property: scriptName
     * {String} The filename of this script without any path.
     */
    scriptName: "proj4js.js",
    /**
     * Property: defsLookupService
     * AJAX service to retreive projection definition parameters from
     */
    defsLookupService: 'http://spatialreference.org/ref',
    /**
     * Property: libPath
     * internal: http server path to library code.
     */
    libPath: null,
    /**
     * Function: getScriptLocation
     * Return the path to this script.
     *
     * Returns:
     * Path to this script
     */
    getScriptLocation: function() {
        if (this.libPath)
            return this.libPath;
        var scriptName = this.scriptName;
        var scriptNameLen = scriptName.length;

        var scripts = document.getElementsByTagName('script');
        for (var i = 0; i < scripts.length; i++) {
            var src = scripts[i].getAttribute('src');
            if (src) {
                var index = src.lastIndexOf(scriptName);
                // is it found, at the end of the URL?
                if ((index > -1) && (index + scriptNameLen == src.length)) {
                    this.libPath = src.slice(0, -scriptNameLen);
                    break;
                }
            }
        }
        return this.libPath || "";
    },
    /**
     * Function: loadScript
     * Load a JS file from a URL into a <script> tag in the page.
     *
     * Parameters:
     * url - {String} The URL containing the script to load
     * onload - {Function} A method to be executed when the script loads successfully
     * onfail - {Function} A method to be executed when there is an error loading the script
     * loadCheck - {Function} A boolean method that checks to see if the script
     *            has loaded.  Typically this just checks for the existance of
     *            an object in the file just loaded.
     */
    loadScript: function(url, onload, onfail, loadCheck) {
        var script = document.createElement('script');
        script.defer = false;
        script.type = "text/javascript";
        script.id = url;
        script.onload = onload;
        script.onerror = onfail;
        script.loadCheck = loadCheck;
        if (/MSIE/.test(navigator.userAgent)) {
            script.onreadystatechange = this.checkReadyState;
        }
        document.getElementsByTagName('head')[0].appendChild(script);
        script.src = url;
    },
    /**
     * Function: checkReadyState
     * IE workaround since there is no onerror handler.  Calls the user defined
     * loadCheck method to determine if the script is loaded.
     *
     */
    checkReadyState: function() {
        if (this.readyState == 'loaded') {
            if (!this.loadCheck()) {
                this.onerror();
            } else {
                this.onload();
            }
        }
    }
};

/**
 * Class: Proj4js.Proj
 *
 * Proj objects provide transformation methods for point coordinates
 * between geodetic latitude/longitude and a projected coordinate system.
 * once they have been initialized with a projection code.
 *
 * Initialization of Proj objects is with a projection code, usually EPSG codes,
 * which is the key that will be used with the Proj4js.defs array.
 *
 * The code passed in will be stripped of colons and converted to uppercase
 * to locate projection definition files.
 *
 * A projection object has properties for units and title strings.
 */
Proj4js.Proj = Proj4js.Class({
    /**
     * Property: readyToUse
     * Flag to indicate if initialization is complete for this Proj object
     */
    readyToUse: false,
    /**
     * Property: title
     * The title to describe the projection
     */
    title: null,
    /**
     * Property: projName
     * The projection class for this projection, e.g. lcc (lambert conformal conic,
     * or merc for mercator).  These are exactly equivalent to their Proj4
     * counterparts.
     */
    projName: null,
    /**
     * Property: units
     * The units of the projection.  Values include 'm' and 'degrees'
     */
    units: null,
    /**
     * Property: datum
     * The datum specified for the projection
     */
    datum: null,
    /**
     * Property: x0
     * The x coordinate origin
     */
    x0: 0,
    /**
     * Property: y0
     * The y coordinate origin
     */
    y0: 0,
    /**
     * Property: localCS
     * Flag to indicate if the projection is a local one in which no transforms
     * are required.
     */
    localCS: false,
    /**
     * Property: queue
     * Buffer (FIFO) to hold callbacks waiting to be called when projection loaded.
     */
    queue: null,
    /**
     * Constructor: initialize
     * Constructor for Proj4js.Proj objects
     *
     * Parameters:
     * srsCode - a code for map projection definition parameters.  These are usually
     * (but not always) EPSG codes.
     */
    initialize: function(srsCode, callback) {
        this.srsCodeInput = srsCode;

        //Register callbacks prior to attempting to process definition
        this.queue = [];
        if (callback) {
            this.queue.push(callback);
        }

        //check to see if this is a WKT string
        if ((srsCode.indexOf('GEOGCS') >= 0) ||
                (srsCode.indexOf('GEOCCS') >= 0) ||
                (srsCode.indexOf('PROJCS') >= 0) ||
                (srsCode.indexOf('LOCAL_CS') >= 0)) {
            this.parseWKT(srsCode);
            this.deriveConstants();
            this.loadProjCode(this.projName);
            return;
        }

        // DGR 2008-08-03 : support urn and url
        if (srsCode.indexOf('urn:') == 0) {
            //urn:ORIGINATOR:def:crs:CODESPACE:VERSION:ID
            var urn = srsCode.split(':');
            if ((urn[1] == 'ogc' || urn[1] == 'x-ogc') &&
                    (urn[2] == 'def') &&
                    (urn[3] == 'crs')) {
                srsCode = urn[4] + ':' + urn[urn.length - 1];
            }
        } else if (srsCode.indexOf('http://') == 0) {
            //url#ID
            var url = srsCode.split('#');
            if (url[0].match(/epsg.org/)) {
                // http://www.epsg.org/#
                srsCode = 'EPSG:' + url[1];
            } else if (url[0].match(/RIG.xml/)) {
                //http://librairies.ign.fr/geoportail/resources/RIG.xml#
                //http://interop.ign.fr/registers/ign/RIG.xml#
                srsCode = 'IGNF:' + url[1];
            } else if (url[0].indexOf('/def/crs/') != -1) {
                // http://www.opengis.net/def/crs/EPSG/0/code
                url = srsCode.split('/');
                srsCode = url.pop();//code
                url.pop();//version FIXME
                srsCode = url.pop() + ':' + srsCode;//authority
            }
        }
        this.srsCode = srsCode.toUpperCase();
        if (this.srsCode.indexOf("EPSG") == 0) {
            this.srsCode = this.srsCode;
            this.srsAuth = 'epsg';
            this.srsProjNumber = this.srsCode.substring(5);
            // DGR 2007-11-20 : authority IGNF
        } else if (this.srsCode.indexOf("IGNF") == 0) {
            this.srsCode = this.srsCode;
            this.srsAuth = 'IGNF';
            this.srsProjNumber = this.srsCode.substring(5);
            // DGR 2008-06-19 : pseudo-authority CRS for WMS
        } else if (this.srsCode.indexOf("CRS") == 0) {
            this.srsCode = this.srsCode;
            this.srsAuth = 'CRS';
            this.srsProjNumber = this.srsCode.substring(4);
        } else {
            this.srsAuth = '';
            this.srsProjNumber = this.srsCode;
        }

        this.loadProjDefinition();
    },
    /**
     * Function: loadProjDefinition
     *    Loads the coordinate system initialization string if required.
     *    Note that dynamic loading happens asynchronously so an application must
     *    wait for the readyToUse property is set to true.
     *    To prevent dynamic loading, include the defs through a script tag in
     *    your application.
     *
     */
    loadProjDefinition: function() {
        //check in memory
        if (Proj4js.defs[this.srsCode]) {
            this.defsLoaded();
            return;
        }

        //else check for def on the server
        var url = Proj4js.getScriptLocation() + 'defs/' + this.srsAuth.toUpperCase() + this.srsProjNumber + '.js';
        Proj4js.loadScript(url,
                Proj4js.bind(this.defsLoaded, this),
                Proj4js.bind(this.loadFromService, this),
                Proj4js.bind(this.checkDefsLoaded, this));
    },
    /**
     * Function: loadFromService
     *    Creates the REST URL for loading the definition from a web service and
     *    loads it.
     *
     */
    loadFromService: function() {
        //else load from web service
        var url = Proj4js.defsLookupService + '/' + this.srsAuth + '/' + this.srsProjNumber + '/proj4js/';
        Proj4js.loadScript(url,
                Proj4js.bind(this.defsLoaded, this),
                Proj4js.bind(this.defsFailed, this),
                Proj4js.bind(this.checkDefsLoaded, this));
    },
    /**
     * Function: defsLoaded
     * Continues the Proj object initilization once the def file is loaded
     *
     */
    defsLoaded: function() {
        this.parseDefs();
        this.loadProjCode(this.projName);
    },
    /**
     * Function: checkDefsLoaded
     *    This is the loadCheck method to see if the def object exists
     *
     */
    checkDefsLoaded: function() {
        if (Proj4js.defs[this.srsCode]) {
            return true;
        } else {
            return false;
        }
    },
    /**
     * Function: defsFailed
     *    Report an error in loading the defs file, but continue on using WGS84
     *
     */
    defsFailed: function() {
        Proj4js.reportError('failed to load projection definition for: ' + this.srsCode);
        Proj4js.defs[this.srsCode] = Proj4js.defs['WGS84'];  //set it to something so it can at least continue
        this.defsLoaded();
    },
    /**
     * Function: loadProjCode
     *    Loads projection class code dynamically if required.
     *     Projection code may be included either through a script tag or in
     *     a built version of proj4js
     *
     */
    loadProjCode: function(projName) {
        if (Proj4js.Proj[projName]) {
            this.initTransforms();
            return;
        }

        //the URL for the projection code
        var url = Proj4js.getScriptLocation() + 'projCode/' + projName + '.js';
        Proj4js.loadScript(url,
                Proj4js.bind(this.loadProjCodeSuccess, this, projName),
                Proj4js.bind(this.loadProjCodeFailure, this, projName),
                Proj4js.bind(this.checkCodeLoaded, this, projName));
    },
    /**
     * Function: loadProjCodeSuccess
     *    Loads any proj dependencies or continue on to final initialization.
     *
     */
    loadProjCodeSuccess: function(projName) {
        if (Proj4js.Proj[projName].dependsOn) {
            this.loadProjCode(Proj4js.Proj[projName].dependsOn);
        } else {
            this.initTransforms();
        }
    },
    /**
     * Function: defsFailed
     *    Report an error in loading the proj file.  Initialization of the Proj
     *    object has failed and the readyToUse flag will never be set.
     *
     */
    loadProjCodeFailure: function(projName) {
        Proj4js.reportError("failed to find projection file for: " + projName);
        //TBD initialize with identity transforms so proj will still work?
    },
    /**
     * Function: checkCodeLoaded
     *    This is the loadCheck method to see if the projection code is loaded
     *
     */
    checkCodeLoaded: function(projName) {
        if (Proj4js.Proj[projName]) {
            return true;
        } else {
            return false;
        }
    },
    /**
     * Function: initTransforms
     *    Finalize the initialization of the Proj object
     *
     */
    initTransforms: function() {
        Proj4js.extend(this, Proj4js.Proj[this.projName]);
        this.init();
        this.readyToUse = true;
        if (this.queue) {
            var item;
            while ((item = this.queue.shift())) {
                item.call(this, this);
            }
        }
    },
    /**
     * Function: parseWKT
     * Parses a WKT string to get initialization parameters
     *
     */
    wktRE: /^(\w+)\[(.*)\]$/,
    parseWKT: function(wkt) {
        var wktMatch = wkt.match(this.wktRE);
        if (!wktMatch)
            return;
        var wktObject = wktMatch[1];
        var wktContent = wktMatch[2];
        var wktTemp = wktContent.split(",");
        var wktName;
        if (wktObject.toUpperCase() == "TOWGS84") {
            wktName = wktObject;  //no name supplied for the TOWGS84 array
        } else {
            wktName = wktTemp.shift();
        }
        wktName = wktName.replace(/^\"/, "");
        wktName = wktName.replace(/\"$/, "");

        /*
         wktContent = wktTemp.join(",");
         var wktArray = wktContent.split("],");
         for (var i=0; i<wktArray.length-1; ++i) {
         wktArray[i] += "]";
         }
         */

        var wktArray = new Array();
        var bkCount = 0;
        var obj = "";
        for (var i = 0; i < wktTemp.length; ++i) {
            var token = wktTemp[i];
            for (var j = 0; j < token.length; ++j) {
                if (token.charAt(j) == "[")
                    ++bkCount;
                if (token.charAt(j) == "]")
                    --bkCount;
            }
            obj += token;
            if (bkCount === 0) {
                wktArray.push(obj);
                obj = "";
            } else {
                obj += ",";
            }
        }

        //do something based on the type of the wktObject being parsed
        //add in variations in the spelling as required
        switch (wktObject) {
            case 'LOCAL_CS':
                this.projName = 'identity';
                this.localCS = true;
                this.srsCode = wktName;
                break;
            case 'GEOGCS':
                this.projName = 'longlat';
                this.geocsCode = wktName;
                if (!this.srsCode)
                    this.srsCode = wktName;
                break;
            case 'PROJCS':
                this.srsCode = wktName;
                break;
            case 'GEOCCS':
                break;
            case 'PROJECTION':
                this.projName = Proj4js.wktProjections[wktName];
                break;
            case 'DATUM':
                this.datumName = wktName;
                break;
            case 'LOCAL_DATUM':
                this.datumCode = 'none';
                break;
            case 'SPHEROID':
                this.ellps = wktName;
                this.a = parseFloat(wktArray.shift());
                this.rf = parseFloat(wktArray.shift());
                break;
            case 'PRIMEM':
                this.from_greenwich = parseFloat(wktArray.shift()); //to radians?
                break;
            case 'UNIT':
                this.units = wktName;
                this.unitsPerMeter = parseFloat(wktArray.shift());
                break;
            case 'PARAMETER':
                var name = wktName.toLowerCase();
                var value = parseFloat(wktArray.shift());
                //there may be many variations on the wktName values, add in case
                //statements as required
                switch (name) {
                    case 'false_easting':
                        this.x0 = value;
                        break;
                    case 'false_northing':
                        this.y0 = value;
                        break;
                    case 'scale_factor':
                        this.k0 = value;
                        break;
                    case 'central_meridian':
                        this.long0 = value * Proj4js.common.D2R;
                        break;
                    case 'latitude_of_origin':
                        this.lat0 = value * Proj4js.common.D2R;
                        break;
                    case 'more_here':
                        break;
                    default:
                        break;
                }
                break;
            case 'TOWGS84':
                this.datum_params = wktArray;
                break;
                //DGR 2010-11-12: AXIS
            case 'AXIS':
                var name = wktName.toLowerCase();
                var value = wktArray.shift();
                switch (value) {
                    case 'EAST' :
                        value = 'e';
                        break;
                    case 'WEST' :
                        value = 'w';
                        break;
                    case 'NORTH':
                        value = 'n';
                        break;
                    case 'SOUTH':
                        value = 's';
                        break;
                    case 'UP'   :
                        value = 'u';
                        break;
                    case 'DOWN' :
                        value = 'd';
                        break;
                    case 'OTHER':
                    default     :
                        value = ' ';
                        break;//FIXME
                }
                if (!this.axis) {
                    this.axis = "enu";
                }
                switch (name) {
                    case 'x':
                        this.axis = value + this.axis.substr(1, 2);
                        break;
                    case 'y':
                        this.axis = this.axis.substr(0, 1) + value + this.axis.substr(2, 1);
                        break;
                    case 'z':
                        this.axis = this.axis.substr(0, 2) + value;
                        break;
                    default :
                        break;
                }
            case 'MORE_HERE':
                break;
            default:
                break;
        }
        for (var i = 0; i < wktArray.length; ++i) {
            this.parseWKT(wktArray[i]);
        }
    },
    /**
     * Function: parseDefs
     * Parses the PROJ.4 initialization string and sets the associated properties.
     *
     */
    parseDefs: function() {
        var re = new RegExp('(title|proj|units|datum|nadgrids|' +
                'ellps|a|b|rf|' +
                'lat_0|lat_1|lat_2|lat_ts|lon_0|lon_1|lon_2|alpha|lonc|' +
                'x_0|y_0|k_0|k|r_a|zone|south|' +
                'towgs84|to_meter|from_greenwich|pm|axis|czech|' +
                'wktext|no_rot|no_off|no_defs)');
        this.defData = Proj4js.defs[this.srsCode];
        var paramName, paramVal;
        if (!this.defData) {
            return;
        }
        var paramArray = this.defData.split("+");

        for (var prop = 0; prop < paramArray.length; prop++) {
            var property = paramArray[prop].split("=");
            paramName = property[0].toLowerCase();
            paramVal = property[1];

            switch (paramName.replace(/\s/gi, "")) {  // trim out spaces
                case "":
                    break;   // throw away nameless parameter
                    // DGR 2012-10-13 : + in title (EPSG:2056: CH1903+ / LV95)
                case "title":
                    this.title = paramVal;
                    while (!paramArray[prop + 1].match(re)) {
                        this.title += '+' + paramArray[++prop];
                    }
                    break;
                case "proj":
                    this.projName = paramVal.replace(/\s/gi, "");
                    break;
                case "units":
                    this.units = paramVal.replace(/\s/gi, "");
                    break;
                case "datum":
                    this.datumCode = paramVal.replace(/\s/gi, "");
                    break;
                    // DGR 2011-03-20 : nagrids -> nadgrids
                case "nadgrids":
                    this.nadgrids = paramVal.replace(/\s/gi, "");
                    break;// DGR 2012-07-29
                case "ellps":
                    this.ellps = paramVal.replace(/\s/gi, "");
                    break;
                case "a":
                    this.a = parseFloat(paramVal);
                    break;  // semi-major radius
                case "b":
                    this.b = parseFloat(paramVal);
                    break;  // semi-minor radius
                    // DGR 2007-11-20
                case "rf":
                    this.rf = parseFloat(paramVal);
                    break; // inverse flattening rf= a/(a-b)
                case "lat_0":
                    this.lat0 = paramVal * Proj4js.common.D2R;
                    break;        // phi0, central latitude
                case "lat_1":
                    this.lat1 = paramVal * Proj4js.common.D2R;
                    break;        //standard parallel 1
                case "lat_2":
                    this.lat2 = paramVal * Proj4js.common.D2R;
                    break;        //standard parallel 2
                case "lat_ts":
                    this.lat_ts = paramVal * Proj4js.common.D2R;
                    break;      // used in merc and eqc
                case "lon_0":
                    this.long0 = paramVal * Proj4js.common.D2R;
                    break;       // lam0, central longitude
                case "lon_1":
                    this.long1 = paramVal * Proj4js.common.D2R;
                    break;
                case "lon_2":
                    this.long2 = paramVal * Proj4js.common.D2R;
                    break;
                case "no_rot":
                    this.no_rot = true;
                    break;
                case "no_off":
                    this.no_off = true;
                    break;
                case "alpha":
                    this.alpha = parseFloat(paramVal) * Proj4js.common.D2R;
                    break;  //for somerc projection
                case "lonc":
                    this.longc = paramVal * Proj4js.common.D2R;
                    break;       //for somerc projection
                case "x_0":
                    this.x0 = parseFloat(paramVal);
                    break;  // false easting
                case "y_0":
                    this.y0 = parseFloat(paramVal);
                    break;  // false northing
                case "k_0":
                    this.k0 = parseFloat(paramVal);
                    break;  // projection scale factor
                case "k":
                    this.k0 = parseFloat(paramVal);
                    break;  // both forms returned
                case "r_a":
                    this.R_A = true;
                    break;                 // sphere--area of ellipsoid
                case "zone":
                    this.zone = parseInt(paramVal, 10);
                    break;  // UTM Zone
                case "south":
                    this.utmSouth = true;
                    break;  // UTM north/south
                case "towgs84":
                    this.datum_params = paramVal.split(",");
                    break;
                case "to_meter":
                    this.to_meter = parseFloat(paramVal);
                    break; // cartesian scaling
                case "from_greenwich":
                    this.from_greenwich = paramVal * Proj4js.common.D2R;
                    break;
                case "czech":
                    this.czech = true;
                    break;
                    // DGR 2008-07-09 : if pm is not a well-known prime meridian take
                    // the value instead of 0.0, then convert to radians
                case "pm":
                    paramVal = paramVal.replace(/\s/gi, "");
                    this.from_greenwich = Proj4js.PrimeMeridian[paramVal] ?
                            Proj4js.PrimeMeridian[paramVal] : parseFloat(paramVal);
                    this.from_greenwich *= Proj4js.common.D2R;
                    break;
                    // DGR 2010-11-12: axis
                case "axis":
                    paramVal = paramVal.replace(/\s/gi, "");
                    var legalAxis = "ewnsud";
                    if (paramVal.length == 3 &&
                            legalAxis.indexOf(paramVal.substr(0, 1)) != -1 &&
                            legalAxis.indexOf(paramVal.substr(1, 1)) != -1 &&
                            legalAxis.indexOf(paramVal.substr(2, 1)) != -1) {
                        this.axis = paramVal;
                    } //FIXME: be silent ?
                    break;
                case "wktext":
                    break;//DGR 2012-07-29
                case "no_defs":
                    break;
                default: //alert("Unrecognized parameter: " + paramName);
            } // switch()
        } // for paramArray
        this.deriveConstants();
    },
    /**
     * Function: deriveConstants
     * Sets several derived constant values and initialization of datum and ellipse
     *     parameters.
     *
     */
    deriveConstants: function() {
        // DGR 2011-03-20 : nagrids -> nadgrids
        if (this.nadgrids && this.nadgrids.length == 0) {
            this.nadgrids = null;
        }
        if (this.nadgrids) {
            this.grids = this.nadgrids.split(",");
            var g = null, l = this.grids.length;
            if (l > 0) {
                for (var i = 0; i < l; i++) {
                    g = this.grids[i];
                    var fg = g.split("@");
                    if (fg[fg.length - 1] == "") {
                        Proj4js.reportError("nadgrids syntax error '" + this.nadgrids + "' : empty grid found");
                        continue;
                    }
                    this.grids[i] = {
                        mandatory: fg.length == 1, //@=> optional grid (no error if not found)
                        name: fg[fg.length - 1],
                        grid: Proj4js.grids[fg[fg.length - 1]]//FIXME: grids loading ...
                    };
                    if (this.grids[i].mandatory && !this.grids[i].grid) {
                        Proj4js.reportError("Missing '" + this.grids[i].name + "'");
                    }
                }
            }
            // DGR, 2011-03-20: grids is an array of objects that hold
            // the loaded grids, its name and the mandatory informations of it.
        }
        if (this.datumCode && this.datumCode != 'none') {
            var datumDef = Proj4js.Datum[this.datumCode];
            if (datumDef) {
                this.datum_params = datumDef.towgs84 ? datumDef.towgs84.split(',') : null;
                this.ellps = datumDef.ellipse;
                this.datumName = datumDef.datumName ? datumDef.datumName : this.datumCode;
            }
        }
        if (!this.a) {    // do we have an ellipsoid?
            var ellipse = Proj4js.Ellipsoid[this.ellps] ? Proj4js.Ellipsoid[this.ellps] : Proj4js.Ellipsoid['WGS84'];
            Proj4js.extend(this, ellipse);
        }
        if (this.rf && !this.b)
            this.b = (1.0 - 1.0 / this.rf) * this.a;
        if (this.rf === 0 || Math.abs(this.a - this.b) < Proj4js.common.EPSLN) {
            this.sphere = true;
            this.b = this.a;
        }
        this.a2 = this.a * this.a;          // used in geocentric
        this.b2 = this.b * this.b;          // used in geocentric
        this.es = (this.a2 - this.b2) / this.a2;  // e ^ 2
        this.e = Math.sqrt(this.es);        // eccentricity
        if (this.R_A) {
            this.a *= 1. - this.es * (Proj4js.common.SIXTH + this.es * (Proj4js.common.RA4 + this.es * Proj4js.common.RA6));
            this.a2 = this.a * this.a;
            this.b2 = this.b * this.b;
            this.es = 0.;
        }
        this.ep2 = (this.a2 - this.b2) / this.b2; // used in geocentric
        if (!this.k0)
            this.k0 = 1.0;    //default value
        //DGR 2010-11-12: axis
        if (!this.axis) {
            this.axis = "enu";
        }

        this.datum = new Proj4js.datum(this);
    }
});

Proj4js.Proj.longlat = {
    init: function() {
        //no-op for longlat
    },
    forward: function(pt) {
        //identity transform
        return pt;
    },
    inverse: function(pt) {
        //identity transform
        return pt;
    }
};
Proj4js.Proj.identity = Proj4js.Proj.longlat;

/**
 Proj4js.defs is a collection of coordinate system definition objects in the
 PROJ.4 command line format.
 Generally a def is added by means of a separate .js file for example:

 <SCRIPT type="text/javascript" src="defs/EPSG26912.js"></SCRIPT>

 def is a CS definition in PROJ.4 WKT format, for example:
 +proj="tmerc"   //longlat, etc.
 +a=majorRadius
 +b=minorRadius
 +lat0=somenumber
 +long=somenumber
 */
Proj4js.defs = {
    // These are so widely used, we'll go ahead and throw them in
    // without requiring a separate .js file
    'WGS84': "+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees",
    'EPSG:4326': "+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees",
    'EPSG:4269': "+title=NAD83 (long/lat) +proj=longlat +a=6378137.0 +b=6356752.31414036 +ellps=GRS80 +datum=NAD83 +units=degrees",
    'EPSG:3857': "+title=WGS 84 / Pseudo-Mercator +proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs"
};
Proj4js.defs['EPSG:3785'] = Proj4js.defs['EPSG:3857'];  //maintain backward compat, official code is 3857
Proj4js.defs['GOOGLE'] = Proj4js.defs['EPSG:3857'];
Proj4js.defs['EPSG:900913'] = Proj4js.defs['EPSG:3857'];
Proj4js.defs['EPSG:102113'] = Proj4js.defs['EPSG:3857'];

Proj4js.common = {
    PI: 3.141592653589793238, //Math.PI,
    HALF_PI: 1.570796326794896619, //Math.PI*0.5,
    TWO_PI: 6.283185307179586477, //Math.PI*2,
    FORTPI: 0.78539816339744833,
    R2D: 57.29577951308232088,
    D2R: 0.01745329251994329577,
    SEC_TO_RAD: 4.84813681109535993589914102357e-6, /* SEC_TO_RAD = Pi/180/3600 */
    EPSLN: 1.0e-10,
    MAX_ITER: 20,
    // following constants from geocent.c
    COS_67P5: 0.38268343236508977, /* cosine of 67.5 degrees */
    AD_C: 1.0026000, /* Toms region 1 constant */

    /* datum_type values */
    PJD_UNKNOWN: 0,
    PJD_3PARAM: 1,
    PJD_7PARAM: 2,
    PJD_GRIDSHIFT: 3,
    PJD_WGS84: 4, // WGS84 or equivalent
    PJD_NODATUM: 5, // WGS84 or equivalent
    SRS_WGS84_SEMIMAJOR: 6378137.0, // only used in grid shift transforms
    SRS_WGS84_ESQUARED: 0.006694379990141316, //DGR: 2012-07-29

    // ellipoid pj_set_ell.c
    SIXTH: .1666666666666666667, /* 1/6 */
    RA4: .04722222222222222222, /* 17/360 */
    RA6: .02215608465608465608, /* 67/3024 */
    RV4: .06944444444444444444, /* 5/72 */
    RV6: .04243827160493827160, /* 55/1296 */

// Function to compute the constant small m which is the radius of
//   a parallel of latitude, phi, divided by the semimajor axis.
// -----------------------------------------------------------------
    msfnz: function(eccent, sinphi, cosphi) {
        var con = eccent * sinphi;
        return cosphi / (Math.sqrt(1.0 - con * con));
    },
// Function to compute the constant small t for use in the forward
//   computations in the Lambert Conformal Conic and the Polar
//   Stereographic projections.
// -----------------------------------------------------------------
    tsfnz: function(eccent, phi, sinphi) {
        var con = eccent * sinphi;
        var com = .5 * eccent;
        con = Math.pow(((1.0 - con) / (1.0 + con)), com);
        return (Math.tan(.5 * (this.HALF_PI - phi)) / con);
    },
// Function to compute the latitude angle, phi2, for the inverse of the
//   Lambert Conformal Conic and Polar Stereographic projections.
// ----------------------------------------------------------------
    phi2z: function(eccent, ts) {
        var eccnth = .5 * eccent;
        var con, dphi;
        var phi = this.HALF_PI - 2 * Math.atan(ts);
        for (var i = 0; i <= 15; i++) {
            con = eccent * Math.sin(phi);
            dphi = this.HALF_PI - 2 * Math.atan(ts * (Math.pow(((1.0 - con) / (1.0 + con)), eccnth))) - phi;
            phi += dphi;
            if (Math.abs(dphi) <= .0000000001)
                return phi;
        }
        alert("phi2z has NoConvergence");
        return (-9999);
    },
    /* Function to compute constant small q which is the radius of a
     parallel of latitude, phi, divided by the semimajor axis.
     ------------------------------------------------------------*/
    qsfnz: function(eccent, sinphi) {
        var con;
        if (eccent > 1.0e-7) {
            con = eccent * sinphi;
            return ((1.0 - eccent * eccent) * (sinphi / (1.0 - con * con) - (.5 / eccent) * Math.log((1.0 - con) / (1.0 + con))));
        } else {
            return(2.0 * sinphi);
        }
    },
    /* Function to compute the inverse of qsfnz
     ------------------------------------------------------------*/
    iqsfnz: function(eccent, q) {
        var temp = 1.0 - (1.0 - eccent * eccent) / (2.0 * eccent) * Math.log((1 - eccent) / (1 + eccent));
        if (Math.abs(Math.abs(q) - temp) < 1.0E-6) {
            if (q < 0.0) {
                return (-1.0 * Proj4js.common.HALF_PI);
            } else {
                return Proj4js.common.HALF_PI;
            }
        }
        //var phi = 0.5* q/(1-eccent*eccent);
        var phi = Math.asin(0.5 * q);
        var dphi;
        var sin_phi;
        var cos_phi;
        var con;
        for (var i = 0; i < 30; i++) {
            sin_phi = Math.sin(phi);
            cos_phi = Math.cos(phi);
            con = eccent * sin_phi;
            dphi = Math.pow(1.0 - con * con, 2.0) / (2.0 * cos_phi) * (q / (1 - eccent * eccent) - sin_phi / (1.0 - con * con) + 0.5 / eccent * Math.log((1.0 - con) / (1.0 + con)));
            phi += dphi;
            if (Math.abs(dphi) <= .0000000001) {
                return phi;
            }
        }

        alert("IQSFN-CONV:Latitude failed to converge after 30 iterations");
        return (NaN);
    },
    /* Function to eliminate roundoff errors in asin
     ----------------------------------------------*/
    asinz: function(x) {
        if (Math.abs(x) > 1.0) {
            x = (x > 1.0) ? 1.0 : -1.0;
        }
        return Math.asin(x);
    },
// following functions from gctpc cproj.c for transverse mercator projections
    e0fn: function(x) {
        return(1.0 - 0.25 * x * (1.0 + x / 16.0 * (3.0 + 1.25 * x)));
    },
    e1fn: function(x) {
        return(0.375 * x * (1.0 + 0.25 * x * (1.0 + 0.46875 * x)));
    },
    e2fn: function(x) {
        return(0.05859375 * x * x * (1.0 + 0.75 * x));
    },
    e3fn: function(x) {
        return(x * x * x * (35.0 / 3072.0));
    },
    mlfn: function(e0, e1, e2, e3, phi) {
        return(e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi));
    },
    imlfn: function(ml, e0, e1, e2, e3) {
        var phi;
        var dphi;

        phi = ml / e0;
        for (var i = 0; i < 15; i++) {
            dphi = (ml - (e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi))) / (e0 - 2.0 * e1 * Math.cos(2.0 * phi) + 4.0 * e2 * Math.cos(4.0 * phi) - 6.0 * e3 * Math.cos(6.0 * phi));
            phi += dphi;
            if (Math.abs(dphi) <= .0000000001) {
                return phi;
            }
        }

        Proj4js.reportError("IMLFN-CONV:Latitude failed to converge after 15 iterations");
        return NaN;
    },
    srat: function(esinp, exp) {
        return(Math.pow((1.0 - esinp) / (1.0 + esinp), exp));
    },
// Function to return the sign of an argument
    sign: function(x) {
        if (x < 0.0)
            return(-1);
        else
            return(1);
    },
// Function to adjust longitude to -180 to 180; input in radians
    adjust_lon: function(x) {
        x = (Math.abs(x) < this.PI) ? x : (x - (this.sign(x) * this.TWO_PI));
        return x;
    },
// IGNF - DGR : algorithms used by IGN France

// Function to adjust latitude to -90 to 90; input in radians
    adjust_lat: function(x) {
        x = (Math.abs(x) < this.HALF_PI) ? x : (x - (this.sign(x) * this.PI));
        return x;
    },
// Latitude Isometrique - close to tsfnz ...
    latiso: function(eccent, phi, sinphi) {
        if (Math.abs(phi) > this.HALF_PI)
            return +Number.NaN;
        if (phi == this.HALF_PI)
            return Number.POSITIVE_INFINITY;
        if (phi == -1.0 * this.HALF_PI)
            return -1.0 * Number.POSITIVE_INFINITY;

        var con = eccent * sinphi;
        return Math.log(Math.tan((this.HALF_PI + phi) / 2.0)) + eccent * Math.log((1.0 - con) / (1.0 + con)) / 2.0;
    },
    fL: function(x, L) {
        return 2.0 * Math.atan(x * Math.exp(L)) - this.HALF_PI;
    },
// Inverse Latitude Isometrique - close to ph2z
    invlatiso: function(eccent, ts) {
        var phi = this.fL(1.0, ts);
        var Iphi = 0.0;
        var con = 0.0;
        do {
            Iphi = phi;
            con = eccent * Math.sin(Iphi);
            phi = this.fL(Math.exp(eccent * Math.log((1.0 + con) / (1.0 - con)) / 2.0), ts);
        } while (Math.abs(phi - Iphi) > 1.0e-12);
        return phi;
    },
// Needed for Gauss Schreiber
// Original:  Denis Makarov (info@binarythings.com)
// Web Site:  http://www.binarythings.com
    sinh: function(x)
    {
        var r = Math.exp(x);
        r = (r - 1.0 / r) / 2.0;
        return r;
    },
    cosh: function(x)
    {
        var r = Math.exp(x);
        r = (r + 1.0 / r) / 2.0;
        return r;
    },
    tanh: function(x)
    {
        var r = Math.exp(x);
        r = (r - 1.0 / r) / (r + 1.0 / r);
        return r;
    },
    asinh: function(x)
    {
        var s = (x >= 0 ? 1.0 : -1.0);
        return s * (Math.log(Math.abs(x) + Math.sqrt(x * x + 1.0)));
    },
    acosh: function(x)
    {
        return 2.0 * Math.log(Math.sqrt((x + 1.0) / 2.0) + Math.sqrt((x - 1.0) / 2.0));
    },
    atanh: function(x)
    {
        return Math.log((x - 1.0) / (x + 1.0)) / 2.0;
    },
// Grande Normale
    gN: function(a, e, sinphi)
    {
        var temp = e * sinphi;
        return a / Math.sqrt(1.0 - temp * temp);
    },
    //code from the PROJ.4 pj_mlfn.c file;  this may be useful for other projections
    pj_enfn: function(es) {
        var en = new Array();
        en[0] = this.C00 - es * (this.C02 + es * (this.C04 + es * (this.C06 + es * this.C08)));
        en[1] = es * (this.C22 - es * (this.C04 + es * (this.C06 + es * this.C08)));
        var t = es * es;
        en[2] = t * (this.C44 - es * (this.C46 + es * this.C48));
        t *= es;
        en[3] = t * (this.C66 - es * this.C68);
        en[4] = t * es * this.C88;
        return en;
    },
    pj_mlfn: function(phi, sphi, cphi, en) {
        cphi *= sphi;
        sphi *= sphi;
        return(en[0] * phi - cphi * (en[1] + sphi * (en[2] + sphi * (en[3] + sphi * en[4]))));
    },
    pj_inv_mlfn: function(arg, es, en) {
        var k = 1. / (1. - es);
        var phi = arg;
        for (var i = Proj4js.common.MAX_ITER; i; --i) { /* rarely goes over 2 iterations */
            var s = Math.sin(phi);
            var t = 1. - es * s * s;
            //t = this.pj_mlfn(phi, s, Math.cos(phi), en) - arg;
            //phi -= t * (t * Math.sqrt(t)) * k;
            t = (this.pj_mlfn(phi, s, Math.cos(phi), en) - arg) * (t * Math.sqrt(t)) * k;
            phi -= t;
            if (Math.abs(t) < Proj4js.common.EPSLN)
                return phi;
        }
        Proj4js.reportError("cass:pj_inv_mlfn: Convergence error");
        return phi;
    },
    /**
     * Determine correction values
     * source: nad_intr.c (DGR: 2012-07-29)
     */
    nad_intr: function(pin, ct) {
        // force computation by decreasing by 1e-7 to be as closed as possible
        // from computation under C:C++ by leveraging rounding problems ...
        var t = {"x": (pin.x - 1.e-7) / ct.del[0], "y": (pin.y - 1e-7) / ct.del[1]};
        var indx = {"x": Math.floor(t.x), "y": Math.floor(t.y)};
        var frct = {"x": t.x - 1.0 * indx.x, "y": t.y - 1.0 * indx.y};
        var val = {"x": Number.NaN, "y": Number.NaN};
        var inx;
        if (indx.x < 0) {
            if (!(indx.x == -1 && frct.x > 0.99999999999)) {
                return val;
            }
            ++indx.x;
            frct.x = 0.0;
        } else {
            inx = indx.x + 1;
            if (inx >= ct.lim[0]) {
                if (!(inx == ct.lim[0] && frct.x < 1e-11)) {
                    return val;
                }
                --indx.x;
                frct.x = 1.0;
            }
        }
        if (indx.y < 0) {
            if (!(indx.y == -1 && frct.y > 0.99999999999)) {
                return val;
            }
            ++indx.y;
            frct.y = 0.0;
        } else {
            inx = indx.y + 1;
            if (inx >= ct.lim[1]) {
                if (!(inx == ct.lim[1] && frct.y < 1e-11)) {
                    return val;
                }
                --indx.y;
                frct.y = 1.0;
            }
        }
        inx = (indx.y * ct.lim[0]) + indx.x;
        var f00 = {"x": ct.cvs[inx][0], "y": ct.cvs[inx][1]};
        inx++;
        var f10 = {"x": ct.cvs[inx][0], "y": ct.cvs[inx][1]};
        inx += ct.lim[0];
        var f11 = {"x": ct.cvs[inx][0], "y": ct.cvs[inx][1]};
        inx--;
        var f01 = {"x": ct.cvs[inx][0], "y": ct.cvs[inx][1]};
        var m11 = frct.x * frct.y, m10 = frct.x * (1.0 - frct.y),
                m00 = (1.0 - frct.x) * (1.0 - frct.y), m01 = (1.0 - frct.x) * frct.y;
        val.x = (m00 * f00.x + m10 * f10.x + m01 * f01.x + m11 * f11.x);
        val.y = (m00 * f00.y + m10 * f10.y + m01 * f01.y + m11 * f11.y);
        return val;
    },
    /**
     * Correct value
     * source: nad_cvt.c (DGR: 2012-07-29)
     */
    nad_cvt: function(pin, inverse, ct) {
        var val = {"x": Number.NaN, "y": Number.NaN};
        if (isNaN(pin.x)) {
            return val;
        }
        var tb = {"x": pin.x, "y": pin.y};
        tb.x -= ct.ll[0];
        tb.y -= ct.ll[1];
        tb.x = Proj4js.common.adjust_lon(tb.x - Proj4js.common.PI) + Proj4js.common.PI;
        var t = Proj4js.common.nad_intr(tb, ct);
        if (inverse) {
            if (isNaN(t.x)) {
                return val;
            }
            t.x = tb.x + t.x;
            t.y = tb.y - t.y;
            var i = 9, tol = 1e-12;
            var dif, del;
            do {
                del = Proj4js.common.nad_intr(t, ct);
                if (isNaN(del.x)) {
                    this.reportError("Inverse grid shift iteration failed, presumably at grid edge.  Using first approximation.");
                    break;
                }
                dif = {"x": t.x - del.x - tb.x, "y": t.y + del.y - tb.y};
                t.x -= dif.x;
                t.y -= dif.y;
            } while (i-- && Math.abs(dif.x) > tol && Math.abs(dif.y) > tol);
            if (i < 0) {
                this.reportError("Inverse grid shift iterator failed to converge.");
                return val;
            }
            val.x = Proj4js.common.adjust_lon(t.x + ct.ll[0]);
            val.y = t.y + ct.ll[1];
        } else {
            if (!isNaN(t.x)) {
                val.x = pin.x - t.x;
                val.y = pin.y + t.y;
            }
        }
        return val;
    },
    /* meridinal distance for ellipsoid and inverse
     **	8th degree - accurate to < 1e-5 meters when used in conjuction
     **		with typical major axis values.
     **	Inverse determines phi to EPS (1e-11) radians, about 1e-6 seconds.
     */
    C00: 1.0,
    C02: .25,
    C04: .046875,
    C06: .01953125,
    C08: .01068115234375,
    C22: .75,
    C44: .46875,
    C46: .01302083333333333333,
    C48: .00712076822916666666,
    C66: .36458333333333333333,
    C68: .00569661458333333333,
    C88: .3076171875

};

/** datum object
 */
Proj4js.datum = Proj4js.Class({
    initialize: function(proj) {
        this.datum_type = Proj4js.common.PJD_WGS84;   //default setting
        if (!proj) {
            return;
        }
        if (proj.datumCode && proj.datumCode == 'none') {
            this.datum_type = Proj4js.common.PJD_NODATUM;
        }
        if (proj.datum_params) {
            for (var i = 0; i < proj.datum_params.length; i++) {
                proj.datum_params[i] = parseFloat(proj.datum_params[i]);
            }
            if (proj.datum_params[0] != 0 || proj.datum_params[1] != 0 || proj.datum_params[2] != 0) {
                this.datum_type = Proj4js.common.PJD_3PARAM;
            }
            if (proj.datum_params.length > 3) {
                if (proj.datum_params[3] != 0 || proj.datum_params[4] != 0 ||
                        proj.datum_params[5] != 0 || proj.datum_params[6] != 0) {
                    this.datum_type = Proj4js.common.PJD_7PARAM;
                    proj.datum_params[3] *= Proj4js.common.SEC_TO_RAD;
                    proj.datum_params[4] *= Proj4js.common.SEC_TO_RAD;
                    proj.datum_params[5] *= Proj4js.common.SEC_TO_RAD;
                    proj.datum_params[6] = (proj.datum_params[6] / 1000000.0) + 1.0;
                }
            }
        }
        // DGR 2011-03-21 : nadgrids support
        this.datum_type = proj.grids ?
                Proj4js.common.PJD_GRIDSHIFT
                : this.datum_type;

        this.a = proj.a;    //datum object also uses these values
        this.b = proj.b;
        this.es = proj.es;
        this.ep2 = proj.ep2;
        this.datum_params = proj.datum_params;
        if (this.datum_type == Proj4js.common.PJD_GRIDSHIFT) {
            this.grids = proj.grids;
        }
    },
    /****************************************************************/
    // cs_compare_datums()
    //   Returns TRUE if the two datums match, otherwise FALSE.
    compare_datums: function(dest) {
        if (this.datum_type != dest.datum_type) {
            return false; // false, datums are not equal
        } else if (this.a != dest.a || Math.abs(this.es - dest.es) > 0.000000000050) {
            // the tolerence for es is to ensure that GRS80 and WGS84
            // are considered identical
            return false;
        } else if (this.datum_type == Proj4js.common.PJD_3PARAM) {
            return (this.datum_params[0] == dest.datum_params[0]
                    && this.datum_params[1] == dest.datum_params[1]
                    && this.datum_params[2] == dest.datum_params[2]);
        } else if (this.datum_type == Proj4js.common.PJD_7PARAM) {
            return (this.datum_params[0] == dest.datum_params[0]
                    && this.datum_params[1] == dest.datum_params[1]
                    && this.datum_params[2] == dest.datum_params[2]
                    && this.datum_params[3] == dest.datum_params[3]
                    && this.datum_params[4] == dest.datum_params[4]
                    && this.datum_params[5] == dest.datum_params[5]
                    && this.datum_params[6] == dest.datum_params[6]);
        } else if (this.datum_type == Proj4js.common.PJD_GRIDSHIFT ||
                dest.datum_type == Proj4js.common.PJD_GRIDSHIFT) {
            //alert("ERROR: Grid shift transformations are not implemented.");
            //return false
            //DGR 2012-07-29 lazy ...
            return this.nadgrids == dest.nadgrids;
        } else {
            return true; // datums are equal
        }
    }, // cs_compare_datums()

    /*
     * The function Convert_Geodetic_To_Geocentric converts geodetic coordinates
     * (latitude, longitude, and height) to geocentric coordinates (X, Y, Z),
     * according to the current ellipsoid parameters.
     *
     *    Latitude  : Geodetic latitude in radians                     (input)
     *    Longitude : Geodetic longitude in radians                    (input)
     *    Height    : Geodetic height, in meters                       (input)
     *    X         : Calculated Geocentric X coordinate, in meters    (output)
     *    Y         : Calculated Geocentric Y coordinate, in meters    (output)
     *    Z         : Calculated Geocentric Z coordinate, in meters    (output)
     *
     */
    geodetic_to_geocentric: function(p) {
        var Longitude = p.x;
        var Latitude = p.y;
        var Height = p.z ? p.z : 0;   //Z value not always supplied
        var X;  // output
        var Y;
        var Z;

        var Error_Code = 0;  //  GEOCENT_NO_ERROR;
        var Rn;            /*  Earth radius at location  */
        var Sin_Lat;       /*  Math.sin(Latitude)  */
        var Sin2_Lat;      /*  Square of Math.sin(Latitude)  */
        var Cos_Lat;       /*  Math.cos(Latitude)  */

        /*
         ** Don't blow up if Latitude is just a little out of the value
         ** range as it may just be a rounding issue.  Also removed longitude
         ** test, it should be wrapped by Math.cos() and Math.sin().  NFW for PROJ.4, Sep/2001.
         */
        if (Latitude < -Proj4js.common.HALF_PI && Latitude > -1.001 * Proj4js.common.HALF_PI) {
            Latitude = -Proj4js.common.HALF_PI;
        } else if (Latitude > Proj4js.common.HALF_PI && Latitude < 1.001 * Proj4js.common.HALF_PI) {
            Latitude = Proj4js.common.HALF_PI;
        } else if ((Latitude < -Proj4js.common.HALF_PI) || (Latitude > Proj4js.common.HALF_PI)) {
            /* Latitude out of range */
            Proj4js.reportError('geocent:lat out of range:' + Latitude);
            return null;
        }

        if (Longitude > Proj4js.common.PI)
            Longitude -= (2 * Proj4js.common.PI);
        Sin_Lat = Math.sin(Latitude);
        Cos_Lat = Math.cos(Latitude);
        Sin2_Lat = Sin_Lat * Sin_Lat;
        Rn = this.a / (Math.sqrt(1.0e0 - this.es * Sin2_Lat));
        X = (Rn + Height) * Cos_Lat * Math.cos(Longitude);
        Y = (Rn + Height) * Cos_Lat * Math.sin(Longitude);
        Z = ((Rn * (1 - this.es)) + Height) * Sin_Lat;

        p.x = X;
        p.y = Y;
        p.z = Z;
        return Error_Code;
    }, // cs_geodetic_to_geocentric()


    geocentric_to_geodetic: function(p) {
        /* local defintions and variables */
        /* end-criterium of loop, accuracy of sin(Latitude) */
        var genau = 1.E-12;
        var genau2 = (genau * genau);
        var maxiter = 30;

        var P;        /* distance between semi-minor axis and location */
        var RR;       /* distance between center and location */
        var CT;       /* sin of geocentric latitude */
        var ST;       /* cos of geocentric latitude */
        var RX;
        var RK;
        var RN;       /* Earth radius at location */
        var CPHI0;    /* cos of start or old geodetic latitude in iterations */
        var SPHI0;    /* sin of start or old geodetic latitude in iterations */
        var CPHI;     /* cos of searched geodetic latitude */
        var SPHI;     /* sin of searched geodetic latitude */
        var SDPHI;    /* end-criterium: addition-theorem of sin(Latitude(iter)-Latitude(iter-1)) */
        var At_Pole;     /* indicates location is in polar region */
        var iter;        /* # of continous iteration, max. 30 is always enough (s.a.) */

        var X = p.x;
        var Y = p.y;
        var Z = p.z ? p.z : 0.0;   //Z value not always supplied
        var Longitude;
        var Latitude;
        var Height;

        At_Pole = false;
        P = Math.sqrt(X * X + Y * Y);
        RR = Math.sqrt(X * X + Y * Y + Z * Z);

        /*      special cases for latitude and longitude */
        if (P / this.a < genau) {

            /*  special case, if P=0. (X=0., Y=0.) */
            At_Pole = true;
            Longitude = 0.0;

            /*  if (X,Y,Z)=(0.,0.,0.) then Height becomes semi-minor axis
             *  of ellipsoid (=center of mass), Latitude becomes PI/2 */
            if (RR / this.a < genau) {
                Latitude = Proj4js.common.HALF_PI;
                Height = -this.b;
                return;
            }
        } else {
            /*  ellipsoidal (geodetic) longitude
             *  interval: -PI < Longitude <= +PI */
            Longitude = Math.atan2(Y, X);
        }

        /* --------------------------------------------------------------
         * Following iterative algorithm was developped by
         * "Institut fï¿½r Erdmessung", University of Hannover, July 1988.
         * Internet: www.ife.uni-hannover.de
         * Iterative computation of CPHI,SPHI and Height.
         * Iteration of CPHI and SPHI to 10**-12 radian resp.
         * 2*10**-7 arcsec.
         * --------------------------------------------------------------
         */
        CT = Z / RR;
        ST = P / RR;
        RX = 1.0 / Math.sqrt(1.0 - this.es * (2.0 - this.es) * ST * ST);
        CPHI0 = ST * (1.0 - this.es) * RX;
        SPHI0 = CT * RX;
        iter = 0;

        /* loop to find sin(Latitude) resp. Latitude
         * until |sin(Latitude(iter)-Latitude(iter-1))| < genau */
        do
        {
            iter++;
            RN = this.a / Math.sqrt(1.0 - this.es * SPHI0 * SPHI0);

            /*  ellipsoidal (geodetic) height */
            Height = P * CPHI0 + Z * SPHI0 - RN * (1.0 - this.es * SPHI0 * SPHI0);

            RK = this.es * RN / (RN + Height);
            RX = 1.0 / Math.sqrt(1.0 - RK * (2.0 - RK) * ST * ST);
            CPHI = ST * (1.0 - RK) * RX;
            SPHI = CT * RX;
            SDPHI = SPHI * CPHI0 - CPHI * SPHI0;
            CPHI0 = CPHI;
            SPHI0 = SPHI;
        }
        while (SDPHI * SDPHI > genau2 && iter < maxiter);

        /*      ellipsoidal (geodetic) latitude */
        Latitude = Math.atan(SPHI / Math.abs(CPHI));

        p.x = Longitude;
        p.y = Latitude;
        p.z = Height;
        return p;
    }, // cs_geocentric_to_geodetic()

    /** Convert_Geocentric_To_Geodetic
     * The method used here is derived from 'An Improved Algorithm for
     * Geocentric to Geodetic Coordinate Conversion', by Ralph Toms, Feb 1996
     */
    geocentric_to_geodetic_noniter: function(p) {
        var X = p.x;
        var Y = p.y;
        var Z = p.z ? p.z : 0;   //Z value not always supplied
        var Longitude;
        var Latitude;
        var Height;

        var W;        /* distance from Z axis */
        var W2;       /* square of distance from Z axis */
        var T0;       /* initial estimate of vertical component */
        var T1;       /* corrected estimate of vertical component */
        var S0;       /* initial estimate of horizontal component */
        var S1;       /* corrected estimate of horizontal component */
        var Sin_B0;   /* Math.sin(B0), B0 is estimate of Bowring aux variable */
        var Sin3_B0;  /* cube of Math.sin(B0) */
        var Cos_B0;   /* Math.cos(B0) */
        var Sin_p1;   /* Math.sin(phi1), phi1 is estimated latitude */
        var Cos_p1;   /* Math.cos(phi1) */
        var Rn;       /* Earth radius at location */
        var Sum;      /* numerator of Math.cos(phi1) */
        var At_Pole;  /* indicates location is in polar region */

        X = parseFloat(X);  // cast from string to float
        Y = parseFloat(Y);
        Z = parseFloat(Z);

        At_Pole = false;
        if (X != 0.0)
        {
            Longitude = Math.atan2(Y, X);
        }
        else
        {
            if (Y > 0)
            {
                Longitude = Proj4js.common.HALF_PI;
            }
            else if (Y < 0)
            {
                Longitude = -Proj4js.common.HALF_PI;
            }
            else
            {
                At_Pole = true;
                Longitude = 0.0;
                if (Z > 0.0)
                {  /* north pole */
                    Latitude = Proj4js.common.HALF_PI;
                }
                else if (Z < 0.0)
                {  /* south pole */
                    Latitude = -Proj4js.common.HALF_PI;
                }
                else
                {  /* center of earth */
                    Latitude = Proj4js.common.HALF_PI;
                    Height = -this.b;
                    return;
                }
            }
        }
        W2 = X * X + Y * Y;
        W = Math.sqrt(W2);
        T0 = Z * Proj4js.common.AD_C;
        S0 = Math.sqrt(T0 * T0 + W2);
        Sin_B0 = T0 / S0;
        Cos_B0 = W / S0;
        Sin3_B0 = Sin_B0 * Sin_B0 * Sin_B0;
        T1 = Z + this.b * this.ep2 * Sin3_B0;
        Sum = W - this.a * this.es * Cos_B0 * Cos_B0 * Cos_B0;
        S1 = Math.sqrt(T1 * T1 + Sum * Sum);
        Sin_p1 = T1 / S1;
        Cos_p1 = Sum / S1;
        Rn = this.a / Math.sqrt(1.0 - this.es * Sin_p1 * Sin_p1);
        if (Cos_p1 >= Proj4js.common.COS_67P5)
        {
            Height = W / Cos_p1 - Rn;
        }
        else if (Cos_p1 <= -Proj4js.common.COS_67P5)
        {
            Height = W / -Cos_p1 - Rn;
        }
        else
        {
            Height = Z / Sin_p1 + Rn * (this.es - 1.0);
        }
        if (At_Pole == false)
        {
            Latitude = Math.atan(Sin_p1 / Cos_p1);
        }

        p.x = Longitude;
        p.y = Latitude;
        p.z = Height;
        return p;
    }, // geocentric_to_geodetic_noniter()

    /****************************************************************/
    // pj_geocentic_to_wgs84( p )
    //  p = point to transform in geocentric coordinates (x,y,z)
    geocentric_to_wgs84: function(p) {

        if (this.datum_type == Proj4js.common.PJD_3PARAM)
        {
            // if( x[io] == HUGE_VAL )
            //    continue;
            p.x += this.datum_params[0];
            p.y += this.datum_params[1];
            p.z += this.datum_params[2];

        }
        else if (this.datum_type == Proj4js.common.PJD_7PARAM)
        {
            var Dx_BF = this.datum_params[0];
            var Dy_BF = this.datum_params[1];
            var Dz_BF = this.datum_params[2];
            var Rx_BF = this.datum_params[3];
            var Ry_BF = this.datum_params[4];
            var Rz_BF = this.datum_params[5];
            var M_BF = this.datum_params[6];
            // if( x[io] == HUGE_VAL )
            //    continue;
            var x_out = M_BF * (p.x - Rz_BF * p.y + Ry_BF * p.z) + Dx_BF;
            var y_out = M_BF * (Rz_BF * p.x + p.y - Rx_BF * p.z) + Dy_BF;
            var z_out = M_BF * (-Ry_BF * p.x + Rx_BF * p.y + p.z) + Dz_BF;
            p.x = x_out;
            p.y = y_out;
            p.z = z_out;
        }
    }, // cs_geocentric_to_wgs84

    /****************************************************************/
    // pj_geocentic_from_wgs84()
    //  coordinate system definition,
    //  point to transform in geocentric coordinates (x,y,z)
    geocentric_from_wgs84: function(p) {

        if (this.datum_type == Proj4js.common.PJD_3PARAM)
        {
            //if( x[io] == HUGE_VAL )
            //    continue;
            p.x -= this.datum_params[0];
            p.y -= this.datum_params[1];
            p.z -= this.datum_params[2];

        }
        else if (this.datum_type == Proj4js.common.PJD_7PARAM)
        {
            var Dx_BF = this.datum_params[0];
            var Dy_BF = this.datum_params[1];
            var Dz_BF = this.datum_params[2];
            var Rx_BF = this.datum_params[3];
            var Ry_BF = this.datum_params[4];
            var Rz_BF = this.datum_params[5];
            var M_BF = this.datum_params[6];
            var x_tmp = (p.x - Dx_BF) / M_BF;
            var y_tmp = (p.y - Dy_BF) / M_BF;
            var z_tmp = (p.z - Dz_BF) / M_BF;
            //if( x[io] == HUGE_VAL )
            //    continue;

            p.x = x_tmp + Rz_BF * y_tmp - Ry_BF * z_tmp;
            p.y = -Rz_BF * x_tmp + y_tmp + Rx_BF * z_tmp;
            p.z = Ry_BF * x_tmp - Rx_BF * y_tmp + z_tmp;
        } //cs_geocentric_from_wgs84()
    }
});

/** point object, nothing fancy, just allows values to be
 passed back and forth by reference rather than by value.
 Other point classes may be used as long as they have
 x and y properties, which will get modified in the transform method.
 */
Proj4js.Point = Proj4js.Class({
    /**
     * Constructor: Proj4js.Point
     *
     * Parameters:
     * - x {float} or {Array} either the first coordinates component or
     *     the full coordinates
     * - y {float} the second component
     * - z {float} the third component, optional.
     */
    initialize: function(x, y, z) {
        if (typeof x == 'object') {
            this.x = x[0];
            this.y = x[1];
            this.z = x[2] || 0.0;
        } else if (typeof x == 'string' && typeof y == 'undefined') {
            var coords = x.split(',');
            this.x = parseFloat(coords[0]);
            this.y = parseFloat(coords[1]);
            this.z = parseFloat(coords[2]) || 0.0;
        } else {
            this.x = x;
            this.y = y;
            this.z = z || 0.0;
        }
    },
    /**
     * APIMethod: clone
     * Build a copy of a Proj4js.Point object.
     *
     * Return:
     * {Proj4js}.Point the cloned point.
     */
    clone: function() {
        return new Proj4js.Point(this.x, this.y, this.z);
    },
    /**
     * APIMethod: toString
     * Return a readable string version of the point
     *
     * Return:
     * {String} String representation of Proj4js.Point object.
     *           (ex. <i>"x=5,y=42"</i>)
     */
    toString: function() {
        return ("x=" + this.x + ",y=" + this.y);
    },
    /**
     * APIMethod: toShortString
     * Return a short string version of the point.
     *
     * Return:
     * {String} Shortened String representation of Proj4js.Point object.
     *         (ex. <i>"5, 42"</i>)
     */
    toShortString: function() {
        return (this.x + ", " + this.y);
    }
});

Proj4js.PrimeMeridian = {
    "greenwich": 0.0, //"0dE",
    "lisbon": -9.131906111111, //"9d07'54.862\"W",
    "paris": 2.337229166667, //"2d20'14.025\"E",
    "bogota": -74.080916666667, //"74d04'51.3\"W",
    "madrid": -3.687938888889, //"3d41'16.58\"W",
    "rome": 12.452333333333, //"12d27'8.4\"E",
    "bern": 7.439583333333, //"7d26'22.5\"E",
    "jakarta": 106.807719444444, //"106d48'27.79\"E",
    "ferro": -17.666666666667, //"17d40'W",
    "brussels": 4.367975, //"4d22'4.71\"E",
    "stockholm": 18.058277777778, //"18d3'29.8\"E",
    "athens": 23.7163375, //"23d42'58.815\"E",
    "oslo": 10.722916666667   //"10d43'22.5\"E"
};

Proj4js.Ellipsoid = {
    "MERIT": {a: 6378137.0, rf: 298.257, ellipseName: "MERIT 1983"},
    "SGS85": {a: 6378136.0, rf: 298.257, ellipseName: "Soviet Geodetic System 85"},
    "GRS80": {a: 6378137.0, rf: 298.257222101, ellipseName: "GRS 1980(IUGG, 1980)"},
    "IAU76": {a: 6378140.0, rf: 298.257, ellipseName: "IAU 1976"},
    "airy": {a: 6377563.396, b: 6356256.910, ellipseName: "Airy 1830"},
    "APL4.": {a: 6378137, rf: 298.25, ellipseName: "Appl. Physics. 1965"},
    "NWL9D": {a: 6378145.0, rf: 298.25, ellipseName: "Naval Weapons Lab., 1965"},
    "mod_airy": {a: 6377340.189, b: 6356034.446, ellipseName: "Modified Airy"},
    "andrae": {a: 6377104.43, rf: 300.0, ellipseName: "Andrae 1876 (Den., Iclnd.)"},
    "aust_SA": {a: 6378160.0, rf: 298.25, ellipseName: "Australian Natl & S. Amer. 1969"},
    "GRS67": {a: 6378160.0, rf: 298.2471674270, ellipseName: "GRS 67(IUGG 1967)"},
    "bessel": {a: 6377397.155, rf: 299.1528128, ellipseName: "Bessel 1841"},
    "bess_nam": {a: 6377483.865, rf: 299.1528128, ellipseName: "Bessel 1841 (Namibia)"},
    "clrk66": {a: 6378206.4, b: 6356583.8, ellipseName: "Clarke 1866"},
    "clrk80": {a: 6378249.145, rf: 293.4663, ellipseName: "Clarke 1880 mod."},
    "CPM": {a: 6375738.7, rf: 334.29, ellipseName: "Comm. des Poids et Mesures 1799"},
    "delmbr": {a: 6376428.0, rf: 311.5, ellipseName: "Delambre 1810 (Belgium)"},
    "engelis": {a: 6378136.05, rf: 298.2566, ellipseName: "Engelis 1985"},
    "evrst30": {a: 6377276.345, rf: 300.8017, ellipseName: "Everest 1830"},
    "evrst48": {a: 6377304.063, rf: 300.8017, ellipseName: "Everest 1948"},
    "evrst56": {a: 6377301.243, rf: 300.8017, ellipseName: "Everest 1956"},
    "evrst69": {a: 6377295.664, rf: 300.8017, ellipseName: "Everest 1969"},
    "evrstSS": {a: 6377298.556, rf: 300.8017, ellipseName: "Everest (Sabah & Sarawak)"},
    "fschr60": {a: 6378166.0, rf: 298.3, ellipseName: "Fischer (Mercury Datum) 1960"},
    "fschr60m": {a: 6378155.0, rf: 298.3, ellipseName: "Fischer 1960"},
    "fschr68": {a: 6378150.0, rf: 298.3, ellipseName: "Fischer 1968"},
    "helmert": {a: 6378200.0, rf: 298.3, ellipseName: "Helmert 1906"},
    "hough": {a: 6378270.0, rf: 297.0, ellipseName: "Hough"},
    "intl": {a: 6378388.0, rf: 297.0, ellipseName: "International 1909 (Hayford)"},
    "kaula": {a: 6378163.0, rf: 298.24, ellipseName: "Kaula 1961"},
    "lerch": {a: 6378139.0, rf: 298.257, ellipseName: "Lerch 1979"},
    "mprts": {a: 6397300.0, rf: 191.0, ellipseName: "Maupertius 1738"},
    "new_intl": {a: 6378157.5, b: 6356772.2, ellipseName: "New International 1967"},
    "plessis": {a: 6376523.0, rf: 6355863.0, ellipseName: "Plessis 1817 (France)"},
    "krass": {a: 6378245.0, rf: 298.3, ellipseName: "Krassovsky, 1942"},
    "SEasia": {a: 6378155.0, b: 6356773.3205, ellipseName: "Southeast Asia"},
    "walbeck": {a: 6376896.0, b: 6355834.8467, ellipseName: "Walbeck"},
    "WGS60": {a: 6378165.0, rf: 298.3, ellipseName: "WGS 60"},
    "WGS66": {a: 6378145.0, rf: 298.25, ellipseName: "WGS 66"},
    "WGS72": {a: 6378135.0, rf: 298.26, ellipseName: "WGS 72"},
    "WGS84": {a: 6378137.0, rf: 298.257223563, ellipseName: "WGS 84"},
    "sphere": {a: 6370997.0, b: 6370997.0, ellipseName: "Normal Sphere (r=6370997)"}
};

Proj4js.Datum = {
    "WGS84": {towgs84: "0,0,0", ellipse: "WGS84", datumName: "WGS84"},
    "GGRS87": {towgs84: "-199.87,74.79,246.62", ellipse: "GRS80", datumName: "Greek_Geodetic_Reference_System_1987"},
    "NAD83": {towgs84: "0,0,0", ellipse: "GRS80", datumName: "North_American_Datum_1983"},
    "NAD27": {nadgrids: "@conus,@alaska,@ntv2_0.gsb,@ntv1_can.dat", ellipse: "clrk66", datumName: "North_American_Datum_1927"},
    "potsdam": {towgs84: "606.0,23.0,413.0", ellipse: "bessel", datumName: "Potsdam Rauenberg 1950 DHDN"},
    "carthage": {towgs84: "-263.0,6.0,431.0", ellipse: "clark80", datumName: "Carthage 1934 Tunisia"},
    "hermannskogel": {towgs84: "653.0,-212.0,449.0", ellipse: "bessel", datumName: "Hermannskogel"},
    "ire65": {towgs84: "482.530,-130.596,564.557,-1.042,-0.214,-0.631,8.15", ellipse: "mod_airy", datumName: "Ireland 1965"},
    "nzgd49": {towgs84: "59.47,-5.04,187.44,0.47,-0.1,1.024,-4.5993", ellipse: "intl", datumName: "New Zealand Geodetic Datum 1949"},
    "OSGB36": {towgs84: "446.448,-125.157,542.060,0.1502,0.2470,0.8421,-20.4894", ellipse: "airy", datumName: "Airy 1830"}
};

Proj4js.WGS84 = new Proj4js.Proj('WGS84');
Proj4js.Datum['OSB36'] = Proj4js.Datum['OSGB36']; //as returned from spatialreference.org

//lookup table to go from the projection name in WKT to the Proj4js projection name
//build this out as required
Proj4js.wktProjections = {
    "Lambert Tangential Conformal Conic Projection": "lcc",
    "Mercator": "merc",
    "Popular Visualisation Pseudo Mercator": "merc",
    "Mercator_1SP": "merc",
    "Transverse_Mercator": "tmerc",
    "Transverse Mercator": "tmerc",
    "Lambert Azimuthal Equal Area": "laea",
    "Universal Transverse Mercator System": "utm"
};

// Based on proj4 CTABLE  structure :
// FIXME: better to have array instead of object holding longitudes, latitudes members
//        In the former case, one has to document index 0 is longitude and
//        1 is latitude ...
//        In the later case, grid object gets bigger !!!!
//        Solution 1 is chosen based on pj_gridinfo.c
Proj4js.grids = {
    "null": {// name of grid's file
        "ll": [-3.14159265, -1.57079633], // lower-left coordinates in radians (longitude, latitude):
        "del": [3.14159265, 1.57079633], // cell's size in radians (longitude, latitude):
        "lim": [3, 3], // number of nodes in longitude, latitude (including edges):
        "count": 9, // total number of nodes
        "cvs": [// shifts : in ntv2 reverse order : lon, lat in radians ...
            [0.0, 0.0], [0.0, 0.0], [0.0, 0.0], // for (lon= 0; lon<lim[0]; lon++) {
            [0.0, 0.0], [0.0, 0.0], [0.0, 0.0], //   for (lat= 0; lat<lim[1]; lat++) { p= cvs[lat*lim[0]+lon]; }
            [0.0, 0.0], [0.0, 0.0], [0.0, 0.0]  // }
        ]
    }
};
/*******************************************************************************
 NAME                            MERCATOR

 PURPOSE:	Transforms input longitude and latitude to Easting and
 Northing for the Mercator projection.  The
 longitude and latitude must be in radians.  The Easting
 and Northing values will be returned in meters.

 PROGRAMMER              DATE
 ----------              ----
 D. Steinwand, EROS      Nov, 1991
 T. Mittan		Mar, 1993

 ALGORITHM REFERENCES

 1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
 Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
 State Government Printing Office, Washington D.C., 1987.

 2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
 U.S. Geological Survey Professional Paper 1453 , United State Government
 Printing Office, Washington D.C., 1989.
 *******************************************************************************/

//static double r_major = a;		   /* major axis 				*/
//static double r_minor = b;		   /* minor axis 				*/
//static double lon_center = long0;	   /* Center longitude (projection center) */
//static double lat_origin =  lat0;	   /* center latitude			*/
//static double e,es;		           /* eccentricity constants		*/
//static double m1;		               /* small value m			*/
//static double false_northing = y0;   /* y offset in meters			*/
//static double false_easting = x0;	   /* x offset in meters			*/
//scale_fact = k0

Proj4js.Proj.merc = {
    init: function() {
        var con = this.b / this.a;
        this.es = 1.0 - con * con;
        this.e = Math.sqrt(this.es);
        if (this.lat_ts) {
            if (this.sphere) {
                this.k0 = Math.cos(this.lat_ts);
            } else {
                this.k0 = Proj4js.common.msfnz(this.e, Math.sin(this.lat_ts), Math.cos(this.lat_ts));
            }
        } else {
            if (!this.k0) {
                if (this.k) {
                    this.k0 = this.k;
                } else {
                    this.k0 = 1.0;
                }
            }
        }
    },
    /* Mercator forward equations--mapping lat,long to x,y
     --------------------------------------------------*/

    forward: function(p) {
        //alert("ll2m coords : "+coords);
        var lon = p.x;
        var lat = p.y;
        // convert to radians
        if (lat * Proj4js.common.R2D > 90.0 &&
                lat * Proj4js.common.R2D < -90.0 &&
                lon * Proj4js.common.R2D > 180.0 &&
                lon * Proj4js.common.R2D < -180.0) {
            Proj4js.reportError("merc:forward: llInputOutOfRange: " + lon + " : " + lat);
            return null;
        }

        var x, y;
        if (Math.abs(Math.abs(lat) - Proj4js.common.HALF_PI) <= Proj4js.common.EPSLN) {
            Proj4js.reportError("merc:forward: ll2mAtPoles");
            return null;
        } else {
            if (this.sphere) {
                x = this.x0 + this.a * this.k0 * Proj4js.common.adjust_lon(lon - this.long0);
                y = this.y0 + this.a * this.k0 * Math.log(Math.tan(Proj4js.common.FORTPI + 0.5 * lat));
            } else {
                var sinphi = Math.sin(lat);
                var ts = Proj4js.common.tsfnz(this.e, lat, sinphi);
                x = this.x0 + this.a * this.k0 * Proj4js.common.adjust_lon(lon - this.long0);
                y = this.y0 - this.a * this.k0 * Math.log(ts);
            }
            p.x = x;
            p.y = y;
            return p;
        }
    },
    /* Mercator inverse equations--mapping x,y to lat/long
     --------------------------------------------------*/
    inverse: function(p) {

        var x = p.x - this.x0;
        var y = p.y - this.y0;
        var lon, lat;

        if (this.sphere) {
            lat = Proj4js.common.HALF_PI - 2.0 * Math.atan(Math.exp(-y / (this.a * this.k0)));
        } else {
            var ts = Math.exp(-y / (this.a * this.k0));
            lat = Proj4js.common.phi2z(this.e, ts);
            if (lat == -9999) {
                Proj4js.reportError("merc:inverse: lat = -9999");
                return null;
            }
        }
        lon = Proj4js.common.adjust_lon(this.long0 + x / (this.a * this.k0));

        p.x = lon;
        p.y = lat;
        return p;
    }
};

// Google Mercator projection
// Used in combination with GoogleMercator layer type in OpenLayers
//+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs

Proj4js.defs["EPSG:900913"] = "+title=GoogleMercator +proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
