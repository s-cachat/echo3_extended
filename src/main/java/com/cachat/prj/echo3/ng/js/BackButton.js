BackButton = {};

BackButton.Sync = Core.extend(Echo.Sync.Button, {
    $load: function () {
        console.log("BackButton load");
        Echo.Render.registerPeer("BackButton", this);
        componentType : "BackButton";

        if (window.history && history.pushState) {
            window.addEventListener('load', function () {
                console.log("BackButton : add event manager");
                history.pushState(-1, null);
                history.pushState(0, null);
                history.pushState(1, null);
                history.go(-1);

                this.addEventListener('popstate', function (event, state) {
                    state = event.state;
                    if (state) {
                        event = document.createEvent('Event');
                        console.log("BackButton : event ", (state > 0 ? 'next' : 'previous'));
                        history.go(-state);
                    }
                }, false);
            }, false);
        }

    },
    renderAdd: function (update, parentElement) {
        console.log("BackButton renderAdd");

    },

    renderDispose: function (update) {
        console.log("BackButton renderDispose");
    },

    renderUpdate: function (update) {
        console.log("BackButton renderUpdate");
    }
});