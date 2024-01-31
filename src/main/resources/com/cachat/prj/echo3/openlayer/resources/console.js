if (!window.console) {
    window.console = {};
    var methods = ["log", "debug", "warn", "info","trace"];
    for (var i = 0; i < methods.length; i++) {
        console[methods[i]] = function() {
        };
    }
}
if (!window.console.trace){
    window.console.trace=function(){
        window.console.log("Trace unavailable on this browser");
    };
}