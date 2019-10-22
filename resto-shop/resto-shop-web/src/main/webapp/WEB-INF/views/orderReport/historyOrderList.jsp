<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
    .explorationContainer .exploreCanvas.stylableVisualContainerHeader .visualContainerHost {
        min-height: 870px;
    }
</style>

<div id="control" style="width: 100%;height: 800px;">

</div>

<script src="https://npmcdn.com/es6-promise@3.2.1"></script>
<script src="assets/powerbi/powerbi.js"></script>
<script src="assets/powerbi/pi_include.js"></script>
<script>
    if (window.innerHeight) {
        var winHeight = window.innerHeight;
        document.getElementById("control").offsetHeight = window.innerHeight;
    }else if ((document.body) && (document.body.clientHeight)){
        var winHeight = document.body.clientHeight;
        document.getElementById("control").offsetHeight = window.innerHeight;
    }

    var brandName = "${brandName}";
    var shopName = "${shopName}";
    var reportName = "${reportName}";
    getReportView(brandName, shopName, reportName);


</script>

