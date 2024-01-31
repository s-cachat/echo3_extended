
var browser = {   // browser object

    // Version of IE
    // [number / null]
    verIE: null,
    // browser.docModeIE is essentially the document mode for the IE browser.
    // It is the EFFECTIVE version of IE that renders the web page.
    // This tells you the level of HTML/CSS/Image support in IE.
    // It is independent of the user Agent, usually.
    // [number/null]
    //
    // If browser.docModeIE >= 6 for IE Desktop,
    //    then we have IE 6+ in Standards Mode.
    //
    // If browser.docModeIE == 5 for IE Desktop,
    //    then we have IE 6+ in Quirks mode,
    //
    // Note: For IE 5.5, browser.docModeIE == 5.5
    // Note: For IE 5, browser.docModeIE == 5
    //
    // No one uses IE < 6 anymore, so browser.docModeIE == 5 means that
    // IE version >= 6 in Quirks Mode.
    docModeIE: null,
    // The TRUE Version of IE.
    // Independent of browser mode / document mode / navigator.userAgent
    // [string "AA.BB.CCCC.DDDDD" / null]
    verIEtrue: null,
    // Version of IE, derived from navigator.userAgent
    // [number/null]
    //
    // Used as a backup, in case all other detection methods fail.
    verIE_ua: null


}, tmp;


// Detect Internet Explorer
// To detect IE, while being independent of the navigator.userAgent,
// we use a combination of 2 methods:
//
//   a) Look at the document.documentMode. If this property is READ ONLY
//    and is a number >=0, then we have IE 8+.
//    According to Microsoft:
//       When the current document has not yet been determined, documentMode returns a value of
//       zero (0). This usually happens when a document is loading.
//       When a return value of zero is received, try to determine the document
//       compatibility mode at a later time.
//
//   b) See if the browser supports Conditional Compilation.
//    If so, then we have IE < 11.
//
tmp = document.documentMode;
try {
    document.documentMode = "";
}
catch (e) {
}
;

// If we have a number, then IE.
// If not, then if we can see the conditional compilation, then IE.
// Else we have a non-IE browser.
browser.isIE = typeof document.documentMode == "number" ? !0 : eval("/*@cc_on!@*/!1");

// We switch the value back to be unobtrusive for non-IE browsers
try {
    document.documentMode = tmp;
}
catch (e) {
}
;



// We only let IE run this code.
if (browser.isIE)
{
    // IE version from user agent
    //
    // For IE < 11, we look for "MSIE 10.0", etc...
    // For IE 11+, we look for "rv:11.0", etc...
    browser.verIE_ua =
            (/^(?:.*?[^a-zA-Z])??(?:MSIE|rv\s*\:)\s*(\d+\.?\d*)/i).test(navigator.userAgent || "") ?
            parseFloat(RegExp.$1, 10) : null;


    // Get true IE version using clientCaps.
    var e, verTrueFloat, x,
            obj = document.createElement("div"),
            // Array of classids that can give us the IE version
            CLASSID = [
                "{45EA75A0-A269-11D1-B5BF-0000F8051515}", // Internet Explorer Help
                "{3AF36230-A269-11D1-B5BF-0000F8051515}", // Offline Browsing Pack
                "{89820200-ECBD-11CF-8B85-00AA005B4383}"
            ];

    try {
        obj.style.behavior = "url(#default#clientcaps)"
    }
    catch (e) {
    }
    ;

    for (x = 0; x < CLASSID.length; x++)
    {
        try {
            // This works for IE 5.5+.
            // For IE 5, we would need to insert obj into the DOM, then set the behaviour,
            // and then query.
            browser.verIEtrue = obj.getComponentVersion(CLASSID[x], "componentid").replace(/,/g, ".");

            //  docWrite("clientCaps " + CLASSID[x] + ": " + browser.verIEtrue);

        } catch (e) {
        }
        ;

        if (browser.verIEtrue)
            break;

    }
    ;

    // docWrite("clientcaps.platform: " + obj.platform);
    // docWrite("clientcaps.cpuClass: " + obj.cpuClass);
    // docWrite("");




    // Given string "AA.BB.CCCC.DDDD", convert to a floating point number AA.BB
    // If verIEtrue is null, then verTrueFloat is 0.
    verTrueFloat = parseFloat(browser.verIEtrue || "0", 10);



    // For IE 8+, we look at document.documentMode
    //
    // Note: It is unlikely that a web designer would set document.documentMode to
    // some arbitrary value for IE < 8.
    browser.docModeIE = document.documentMode ||
            // If document.documentMode is not defined, then we have IE < 8 Desktop.
                    // We try to artificially create a document mode number.
                            //
                                    // if document.compatMode == "BackCompat", then we have Quirks mode, so return 5.
                                            // document.documentMode == 5 in Quirks mode.
                                                    ((/back/i).test(document.compatMode || "") ? 5 : verTrueFloat) ||
                                                    // Else return version from navigator.userAgent, or null
                                                    browser.verIE_ua;



                                            // [number / null]
                                            //
                                            // Try to use True version first, because this gives the
                                            // actual browser version.
                                            //
                                            // If not available, then use the document mode.
                                            // In most cases, this will be the actual IE version, anyway.
                                            browser.verIE = verTrueFloat || browser.docModeIE;


                                        }
                                        ;

  