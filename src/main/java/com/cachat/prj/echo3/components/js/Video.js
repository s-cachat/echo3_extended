Video = {};
Video = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Video", this);
    },
    componentType: "Video"
});

Video.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("Video", this);
        componentType : "Video";
    },
    _div: null,
    _imgTag: null,
    _videoTag: null,
    _alive: true,
    _intUpdate: null,
    _type: null,
    _imageUrl: null,
    _videoUrl: null,
    _loaded: 0, //date de dernière demande d'update, ou 0 si update terminee
    log: function (msg) {
        if (console && console.log) {
            console.log(msg);
        }
    },
    videoUrl: function () {
        return this._videoUrl + ((this._videoUrl.indexOf("?") < 0) ? "?" : "&") + "ts=" + Date.now();
    },
    imageUrl: function () {
        return this._imageUrl + ((this._imageUrl.indexOf("?") < 0) ? "?" : "&") + "ts=" + Date.now();
    },
    renderSize: function (component, element) {
        v = component.render("height");
        if (v) {
            element.style.height = v;
        }
        v = component.render("width");
        if (v) {
            element.style.width = v;
        } else {
            element.style.width = "auto";
        }
    },
    renderAdd: function (update, parentElement) {
        try {
            this.log("video.renderAdd isIE=" + browser.isIE + ", verIE=" + browser.verIE);
            this._div = document.createElement("div");
            Echo.Sync.renderComponentDefaults(this.component, this._div);
            Extended.renderPositionnable(this.component, this._div);
            parentElement.appendChild(this._div);
            this._type = this.component.render("videoType");
            this._imageUrl = this.component.render("videoImageUrl");
            this._videoUrl = this.component.render("videoVideoUrl");
            var showControls = this.component.render("videoShowControl");
            if (browser.isIE && this._type === "MJPEG") {
                this._type = "JPEG";
            }
            if (this._type) {
                switch (this._type) {
                    case "JPEG":
                    {
                        this.log("setup image tag for jpeg");

                        this._imgTag = document.createElement("img");
                        this._imgTag.src = this.imageUrl();
                        this._imgTag.onerror = this._onTagError();
                        this._imgTag.onload = this._onTagLoad.bind(this);
                        this._setup(this._imgTag);
                        console.log("setup image tag for jpeg", this);
                        this.renderSize(this.component, this._imgTag);
                        break;
                    }
                    case "MJPEG":
                    {
                        this.log("setup image tag for mjpeg");
                        this._imgTag = document.createElement("img");
                        this._imgTag.src = this.videoUrl();
                        this._imgTag.onerror = this._onTagError();
                        this._setup(this._imgTag);
                        console.log("setup image tag for jpeg", this);
                        this.renderSize(this.component, this._imgTag);
                        break;
                    }
                    case "WEBM":
                    {
                        this.log("setup video tag for webm src is " + this._videoUrl);
                        this._videoTag = document.createElement("video");
                        this._videoTag.src = this._videoUrl;
                        this._videoTag.poster = this._imageUrl;
                        this._videoTag.autoplay = true;
                        this._videoTag.controls = !("false" === showControls);
                        this._videoTag.onerror = this._onTagError();
                        this._setup(this._videoTag);
                        this.renderSize(this.component, this._videoTag);
                        break;
                    }
                    default:
                    {
                        this._div.innerHTML = "Unsupported type : " + this._type;
                        this.log("Unsupported type : " + this._type);
                        break;
                    }
                }
            }
            if (this._intUpdate === null) {
                this.log("will set timer");
                this._intUpdate = window.setInterval(this._intUpdateFunction.bind(this), 500);
                this.log("_intUpdate=" + this._intUpdate);
            } else {
                this.log("will use existing timer");
            }
        } catch (e) {
            this.log("Error in renderDisplay");
            this.log(e);
            this.log(e.stack);
        }
    },
    _onTagError: function (evt) {
        this.log("onTagError !" + (new Date()));
        this.log(evt);
    },
    _onTagLoad: function () {
        this.log("onTagLoad !" + (Date.now() - this._loaded));
        this._loaded = 0;
    },
    _intUpdateFunction: function () {
        if (this._type) {
            switch (this._type) {
                case "JPEG":
                {
                    if (this._loaded === 0) {
                        //ok
                    } else if ((Date.now() - this._loaded) > 10000) {
                        this.log("image timeout !");
                    } else {
                        this.log("image skip ! _loaded=" + this._loaded);
                        return;//skip
                    }
                    1
                    this._loaded = Date.now();
                    this._imgTag.src = this.imageUrl();
                    this.log("video jpeg interrupt ! : " + this._imgTag.src + "                 " + (new Date()));
                    break;
                }
                case "MJPEG":
                {
                    if ((Date.now() - this._loaded) > 60000) {
                        this._loaded = Date.now();
                        this._imgTag.src = this.videoUrl();
                        this.log("video mjpeg interrupt !");
                    }
                    break;
                }
                case "WEBM":
                {
                    this.log("video webm interrupt !");
                    break;
                }
                default:
                {
                    this.log("video default interrupt !");
                    break;
                }
            }
        } else {
            if (console)
                console.log("video notype interrupt !");
        }
    },
    _setup: function (tag) {
        tag.style.position = "relative";
        tag.style.top = "0px";
        tag.style.bottom = "0px";
        tag.style.right = "0px";
        tag.style.left = "0px";
        this._div.appendChild(tag);
    },
    _clearListener: function () {
        if (this._videoTag) {
            this._videoTag.onerror = null;
            this._videoTag.onload = null;
        }
        if (this._imgTag) {
            this._imgTag.onerror = null;
            this._imgTag.onload = null;
        }
    },
    renderDispose: function (update) {
        _alive = false;
        this._clearListener();
        this._div = null;
        window.clearInterval(this._intUpdate);
        this._intUpdate = null;
    },
    renderUpdate: function (update) {
        try {
            var element = this._div;
            var containerElement = element.parentNode;
            this._clearListener();
            Echo.Render.renderComponentDispose(update, update.parent);
            containerElement.removeChild(element);
            this.renderAdd(update, containerElement);
            return true;
        } catch (e) {
            if (console)
                console.log(e);
        }
    }
});