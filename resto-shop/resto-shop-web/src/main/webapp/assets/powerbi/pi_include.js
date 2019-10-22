function getReportView(brandName, shopName, reportName) {
    $.get("pi/report?reportName="+reportName, function(report){
//         Read embed applpp.poweication token from Model
        var accessToken = report.token;
//        // Read embed URL from Model
        var embedUrl = report.embedUrl;
//        // Read report Id from Model
        var embedReportId = report.reportId;

        // Get models. models contains enums that can be used.
        var models = window['powerbi-client'].models;

        // Embed configuration used to describe the what and how to embed.
        // This object is used when calling powerbi.embed.
        // This also includes settings and options such as filters.
        // You can find more information at https://github.com/Microsoft/PowerBI-JavaScript/wiki/Embed-Configuration-Details.

        const brandFilter = {
            $schema: "http://powerbi.com/product/schema#advanced",
            target: {
                table: "品牌门店表",
                column: "品牌名称"
            },
            logicalOperator: "Or",
            conditions: [
                {
                    operator: "Contains",
                    value: brandName
                }
            ],
            filterType: models.FilterType.AdvancedFilter
        };

        const shopFilter = {
            $schema: "http://powerbi.com/product/schema#advanced",
            target: {
                table: "品牌门店表",
                column: "门店名称"
            },
            logicalOperator: "Or",
            conditions: [
                {
                    operator: "Contains",
                    value: shopName
                }
            ],
            filterType: models.FilterType.AdvancedFilter
        };

        var config = {
            type: 'report',
            tokenType: models.TokenType.Embed,
            accessToken: accessToken,
            embedUrl: embedUrl,
            id: embedReportId,
            permissions: models.Permissions.All,
           filters: [brandFilter, shopFilter],
//        pageView: "oneColumn",
            settings: {
                filterPaneEnabled: false,
                navContentPaneEnabled: true,
//            layoutType: models.LayoutType.MobilePortrait
            }
        };
        var reportContainer = $('#control')[0];
        // Embed the report and display it within the div container.
        var report = powerbi.embed(reportContainer, config);

    });
}