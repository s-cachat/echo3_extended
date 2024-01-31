/* Flot plugin that adds a function to allow user save the current graph as an image
    by right clicking on the graph and then choose "Save image as ..." to local disk.

Copyright (c) 2013 http://zizhujy.com.
Licensed under the MIT license.

Usage:
    Inside the <head></head> area of your html page, add the following lines:
    
    <script type="text/javascript" src="http://zizhujy.com/Scripts/base64.js"></script>
    <script type="text/javascript" src="http://zizhujy.com/Scripts/drawing/canvas2image.js"></script>
    <script type="text/javascript" src="http://zizhujy.com/Scripts/flot/jquery.flot.saveAsImage.js"></script>

    Now you are all set. Right click on your flot canvas, you will see the "Save image as ..." option.

Online examples:
    http://zizhujy.com/FunctionGrapher is using it, you can try right clicking on the function graphs and
    you will see you can save the image to local disk.

Dependencies:
    This plugin references the base64.js and canvas2image.js.

Customizations:
    The default behavior of this plugin is dynamically creating an image from the flot canvas, and then puts the 
    image above the flot canvas. If you want to add some css effects on to the dynamically created image, you can
    apply whatever css styles on to it, only remember to make sure the css class name is set correspondingly by 
    the options object of this plugin. You can also customize the image format through this options object:

    options: {
        imageClassName: "canvas-image",
        imageFormat: "png"
    }

*/
/* inline canvas2image */
/**
 * covert canvas to image
 * and save the image file
 */

var Canvas2Image = function () {

   // check if support sth.
   var $support = function () {
      var canvas = document.createElement('canvas'),
         ctx = canvas.getContext('2d');

      return {
         canvas: !!ctx,
         imageData: !!ctx.getImageData,
         dataURL: !!canvas.toDataURL,
         btoa: !!window.btoa
      };
   }();

   var downloadMime = 'image/octet-stream';

   function scaleCanvas (canvas, width, height) {
      var w = canvas.width,
         h = canvas.height;
      if (width == undefined) {
         width = w;
      }
      if (height == undefined) {
         height = h;
      }

      var retCanvas = document.createElement('canvas');
      var retCtx = retCanvas.getContext('2d');
      retCanvas.width = width;
      retCanvas.height = height;
      retCtx.drawImage(canvas, 0, 0, w, h, 0, 0, width, height);
      return retCanvas;
   }

   function getDataURL (canvas, type, width, height) {
      canvas = scaleCanvas(canvas, width, height);
      return canvas.toDataURL(type);
   }

   function saveFile (strData) {
      document.location.href = strData;
   }

   function genImage(strData) {
      var img = document.createElement('img');
      img.src = strData;
      return img;
   }
   function fixType (type) {
      type = type.toLowerCase().replace(/jpg/i, 'jpeg');
      var r = type.match(/png|jpeg|bmp|gif/)[0];
      return 'image/' + r;
   }
   function encodeData (data) {
      if (!window.btoa) { throw 'btoa undefined' }
      var str = '';
      if (typeof data == 'string') {
         str = data;
      } else {
         for (var i = 0; i < data.length; i ++) {
            str += String.fromCharCode(data[i]);
         }
      }

      return btoa(str);
   }
   function getImageData (canvas) {
      var w = canvas.width,
         h = canvas.height;
      return canvas.getContext('2d').getImageData(0, 0, w, h);
   }
   function makeURI (strData, type) {
      return 'data:' + type + ';base64,' + strData;
   }


   /**
    * create bitmap image
    * 按照规则生成图片响应头和响应体
    */
   var genBitmapImage = function (data) {
      var imgHeader = [],
         imgInfoHeader = [];
      
      var width = data.width,
         height = data.height;

      imgHeader.push(0x42); // 66 -> B
      imgHeader.push(0x4d); // 77 -> M

      var fsize = width * height * 3 + 54; // header size:54 bytes
      imgHeader.push(fsize % 256); // r
      fsize = Math.floor(fsize / 256);
      imgHeader.push(fsize % 256); // g
      fsize = Math.floor(fsize / 256);
      imgHeader.push(fsize % 256); // b
      fsize = Math.floor(fsize / 256);
      imgHeader.push(fsize % 256); // a

      imgHeader.push(0);
      imgHeader.push(0);
      imgHeader.push(0);
      imgHeader.push(0);

      imgHeader.push(54); // offset -> 6
      imgHeader.push(0);
      imgHeader.push(0);
      imgHeader.push(0);

      // info header
      imgInfoHeader.push(40); // info header size
      imgInfoHeader.push(0);
      imgInfoHeader.push(0);
      imgInfoHeader.push(0);

      // 横向info
      var _width = width;
      imgInfoHeader.push(_width % 256);
      _width = Math.floor(_width / 256);
      imgInfoHeader.push(_width % 256);
      _width = Math.floor(_width / 256);
      imgInfoHeader.push(_width % 256);
      _width = Math.floor(_width / 256);
      imgInfoHeader.push(_width % 256);

      // 纵向info
      var _height = height;
      imgInfoHeader.push(_height % 256);
      _height = Math.floor(_height / 256);
      imgInfoHeader.push(_height % 256);
      _height = Math.floor(_height / 256);
      imgInfoHeader.push(_height % 256);
      _height = Math.floor(_height / 256);
      imgInfoHeader.push(_height % 256);

      imgInfoHeader.push(1);
      imgInfoHeader.push(0);
      imgInfoHeader.push(24); // 24位bitmap
      imgInfoHeader.push(0);

      // no compression
      imgInfoHeader.push(0);
      imgInfoHeader.push(0);
      imgInfoHeader.push(0);
      imgInfoHeader.push(0);

      // pixel data
      var dataSize = width * height * 3;
      imgInfoHeader.push(dataSize % 256);
      dataSize = Math.floor(dataSize / 256);
      imgInfoHeader.push(dataSize % 256);
      dataSize = Math.floor(dataSize / 256);
      imgInfoHeader.push(dataSize % 256);
      dataSize = Math.floor(dataSize / 256);
      imgInfoHeader.push(dataSize % 256);

      // blank space
      for (var i = 0; i < 16; i ++) {
         imgInfoHeader.push(0);
      }

      var padding = (4 - ((width * 3) % 4)) % 4;
      var imgData = data.data;
      var strPixelData = '';
      var y = height;
      do {
         var offsetY = width * (y - 1) * 4;
         var strPixelRow = '';
         for (var x = 0; x < width; x ++) {
            var offsetX = 4 * x;
            strPixelRow += String.fromCharCode(imgData[offsetY + offsetX + 2]);
            strPixelRow += String.fromCharCode(imgData[offsetY + offsetX + 1]);
            strPixelRow += String.fromCharCode(imgData[offsetY + offsetX]);
         }
         for (var n = 0; n < padding; n ++) {
            strPixelRow += String.fromCharCode(0);
         }

         strPixelData += strPixelRow;
      } while(-- y);

      return (encodeData(imgHeader.concat(imgInfoHeader)) + encodeData(strPixelData));

   };

   /**
    * saveAsImage
    * @param canvasElement
    * @param {String} image type
    * @param {Number} [optional] png width
    * @param {Number} [optional] png height
    */
   var saveAsImage = function (canvas, width, height, type) {
      if ($support.canvas && $support.dataURL) {
         if (type == undefined) { type = 'png'; }
         type = fixType(type);
         if (/bmp/.test(type)) {
            var data = getImageData(scaleCanvas(canvas, width, height));
            var strData = genBitmapImage(data);
            saveFile(makeURI(strData, downloadMime));
         } else {
            var strData = getDataURL(canvas, type, width, height);
            saveFile(strData.replace(type, downloadMime));
         }
      
      }
   }

   var convertToImage = function (canvas, width, height, type) {
      if ($support.canvas && $support.dataURL) {
         if (type == undefined) { type = 'png'; }
         type = fixType(type);

         if (/bmp/.test(type)) {
            var data = getImageData(scaleCanvas(canvas, width, height));
            var strData = genBitmapImage(data);
            return genImage(makeURI(strData, 'image/bmp'));
         } else {
            var strData = getDataURL(canvas, type, width, height);
            return genImage(strData);
         }
      }
   }



   return {
      saveAsImage: saveAsImage,
      saveAsPNG: function (canvas, width, height) {
         return saveAsImage(canvas, width, height, 'png');
      },
      saveAsJPEG: function (canvas, width, height) {
         return saveAsImage(canvas, width, height, 'jpeg');       
      },
      saveAsGIF: function (canvas, width, height) {
         return saveAsImage(canvas, width, height, 'gif')         
      },
      saveAsBMP: function (canvas, width, height) {
         return saveAsImage(canvas, width, height, 'bmp');        
      },
      
      convertToImage: convertToImage,
      convertToPNG: function (canvas, width, height) {
         return convertToImage(canvas, width, height, 'png');
      },
      convertToJPEG: function (canvas, width, height) {
         return convertToImage(canvas, width, height, 'jpeg');          
      },
      convertToGIF: function (canvas, width, height) {
         return convertToImage(canvas, width, height, 'gif');          
      },
      convertToBMP: function (canvas, width, height) {
         return convertToImage(canvas, width, height, 'bmp');          
      }
   };

}();
; (function ($, Canvas2Image) {
    var imageCreated = null;

    function init(plot, classes) {
        plot.hooks.bindEvents.push(bindEvents);
        plot.hooks.shutdown.push(shutdown);

        function bindEvents(plot, eventHolder) {
            eventHolder.mousedown(onMouseDown);
        }

        function shutdown(plot, eventHolder) {
            eventHolder.unbind("mousedown", onMouseDown);
        }

        function onMouseDown(e) {
            if (e.button == 2) {
                // Open an API in Canvas2Image, in case you would need to call
                // it to delete the dynamically created image.
                //Canvas2Image.deleteStaleCanvasImage = deleteStaleCanvasImage;
                deleteStaleCanvasImage(plot);
                createImageFromCanvas(plot, plot.getOptions().imageFormat);
                // For ubuntu chrome:
                setTimeout(function () { deleteStaleCanvasImage(plot); }, 500);
            }
        }
    }

    function onMouseUp(plot) {
        setTimeout(function () { deleteStaleCanvasImage(plot); }, 100);
    }

    function deleteStaleCanvasImage(plot) {
        //$(plot.getCanvas()).parent().find("img." + plot.getOptions().imageClassName).unbind("mouseup", onMouseUp).remove();
        $(imageCreated).unbind("mouseup", onMouseUp).remove();
    }

    function createImageFromCanvas(plot, format) {
        var canvas = plot.getCanvas();
        var img = null;
        switch (format.toLowerCase()) {
            case "png":
                img = Canvas2Image.saveAsPNG(canvas, format);
                break;
            case "bmp":
                img = Canvas2Image.saveAsBMP(canvas, format);
                break;
            case "jpeg":
                img = Canvas2Image.saveAsJPEG(canvas, format);
                break;
            default:
                break;
        }

        if (!img) {
            img = Canvas2Image.saveAsPNG(canvas, "png");
        }

        if (!img) {
            img = Canvas2Image.saveAsPNG(canvas, "bmp");
        }

        if (!img) {
            img = Canvas2Image.saveAsJPEG(canvas, "jpeg");
        }

        if (!img) {
            alert(plot.getOptions().notSupportMessage || "Oh Sorry, but this browser is not capable of creating image files, please use PRINT SCREEN key instead!");
            return false;
        }

        $(img).attr("class", plot.getOptions().imageClassName);
        $(img).css({ "border": $(canvas).css("border"), "z-index": "9999", "position": "absolute" });
        $(img).insertBefore($(canvas));
        $(img).mouseup(plot, onMouseUp);

        imageCreated = img;
    }

    var options = {
        imageClassName: "canvas-image",
        imageFormat: "png"
    };

    $.plot.plugins.push({
        init: init,
        options: options,
        name: 'saveAsImage',
        version: '1.3'
    });

})(jQuery, Canvas2Image);
