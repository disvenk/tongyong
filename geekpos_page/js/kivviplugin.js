document.addEventListener("plusready",function()
{
    var _BARCODE='kivviplugin',
        B=window.plus.bridge;
    var kivviplugin={
        PluginActionDisplayQR : function(Argus1, Argus2, Argus3, Argus4)
        {
         	return B.execSync(_BARCODE, "PluginActionDisplayQR", [Argus1, Argus2, Argus3, Argus4]);
        },
        PluginActionPrintQR : function(Argus1, Argus2, Argus3, Argus4)
        {
            return B.execSync(_BARCODE, "PluginActionPrintQR", [Argus1, Argus2, Argus3, Argus4]);
        },
        PluginActionPrintText : function(Argus1, Argus2, Argus3, Argus4)
        {
            return B.execSync(_BARCODE, "PluginActionPrintText", [Argus1, Argus2, Argus3, Argus4]);
        },
    };

    window.plus.kivviplugin = kivviplugin;
},true);