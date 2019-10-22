<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
	th,td{text-align: center;}
</style>
<div id="control">
	<%-- 新增原料开始--%>
	<div class="row form-div" v-show="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">原料管理</span>
					</div>
				</div>
				<div class="portlet-body">
						<input type="hidden" name="id" v-model="m.id" />
						<div class="form-body">
							<div class="form-group row">
								<label class="col-md-2 control-label">类型<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="materialType" v-model="m.materialType" class="bs-select form-control" >
										<option disabled selected value>请选择</option>
										<option  v-for="materialType in materialTypes" value="{{materialType.code}}">
											{{materialType.name}}
										</option>
									</select>
								</div>
								<label class="col-md-2 control-label">一级类别<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="categoryOneId" v-model="m.categoryOneId" class="bs-select form-control" @change="categoryOneIdCh" >
										<option disabled selected value>请选择</option>
										<option  v-for="categoryOne in categoryOnes" value="{{categoryOne.id}}">
											{{categoryOne.categoryName}}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group row" >
								<label class="col-md-2 control-label">二级类别<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="categoryTwoId" v-model="m.categoryTwoId" class="bs-select form-control" @change="categoryTwoIdCh">
										<option disabled selected value>请选择</option>
										<option  v-for="categoryTwo in categoryTwos" value="{{categoryTwo.id}}" v-if="m.categoryOneId == categoryTwo.parentId">
											{{categoryTwo.categoryName}}
										</option>
									</select>
								</div>
								<label class="col-md-2 control-label">品牌<span style="color:#FF0000;">*</span></label>

								<button type="button" class="add btn green" @click="addThirdCategory">新增品牌</button>
								<div class="col-md-3">
									<select name="categoryThirdId" v-model="m.categoryThirdId" class="bs-select form-control" >
										<option disabled selected value>请选择</option>
										<option  v-for="categoryThird in categoryThirds" value="{{categoryThird.id}}" v-if="m.categoryTwoId == categoryThird.parentId">
											{{categoryThird.categoryName}}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group row">
								<label class="col-md-2 control-label">原材料名<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="materialName" v-model="m.materialName"
										   required="required">
								</div>
								<label class="col-md-2 control-label">原材别名</label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="printName" v-model="m.printName">

								</div>
							</div>
							<div class="form-group row">

								<label class="col-md-2 control-label">序号<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="priority" v-model="m.priority"
										   required="required">
								</div>

								<label class="col-md-2 control-label">库存单位<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="specList" v-model="specList" class="bs-select form-control" >
										<option disabled selected value>请选择</option>
										<option  v-for="spec in specLists" value="{{[spec.unitName,spec.id]}}">
											{{spec.unitName}}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group row">
								<label class="col-md-2 control-label">核算规格<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="measureUnit" v-model="m.measureUnit" @change="com"
										   required="required">
								</div>
								<label class="col-md-2 control-label">核算单位<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="unitList" v-model="unitList" class="bs-select form-control" >
										<option disabled selected value>请选择</option>
										<option  v-for="unit in unitLists" value="{{[unit.unitName,unit.id]}}">
											{{unit.unitName}}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group row">
								<label class="col-md-2 control-label">转化规格<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="rate" v-model="m.rate" @change="com"
										   required="required">
								</div>

								<label class="col-md-2 control-label">转化单位<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="convertUnitList" v-model="convertUnitList" class="bs-select form-control" >
										<option disabled selected value>请选择</option>
										<option  v-for="unit in convertUnitLists" value="{{[unit.unitName,unit.id]}}">
											{{unit.unitName}}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group row">

								<label class="col-md-2 control-label">最小单位<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="minMeasureUnit" v-model="m.minMeasureUnit"  required="required" @change="com"
										   style="display: inline-block;text-align: center;width:70%;">
									<span style="text-align: center">{{m.convertUnitName}}</span>
								</div>
								<label class="col-md-2 control-label">最小单位份数</label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="coefficient" :value="coefficients"  v-model="m.coefficient" readOnly="true">
								</div>
							</div>

							<div class="form-group row">
								<label class="col-md-2 control-label">省份</label>
								<input type="hidden" name="provinceName" v-model="m.provinceName" />
								<input type="hidden" name="provinceId" v-model="m.provinceId" />
								<div class="col-md-3">
									<select  v-model="provinceNameList" class="bs-select form-control" @change="provinceNameListCh">
										<option disabled selected value>请选择</option>
										<option  v-for="province in provinceNameLists" value="{{[province.provinceName,province.id]}}">
											{{province.provinceName}}
										</option>
									</select>
								</div>

								<label class="col-md-2 control-label">城市</label>
								<input type="hidden" name="cityName" v-model="m.cityName" />
								<input type="hidden" name="cityId" v-model="m.cityId" />
								<div class="col-md-3">
									<select  v-model="cityNameList" class="bs-select form-control" @change="cityNameListCh">
										<option disabled selected value>请选择</option>
										<option  v-for="city in cityNameLists" value="{{[city.cityName,city.id]}}" v-if="city.provinceId == provinceNameList.split(',')[1]">
											{{city.cityName}}
										</option>
									</select>
								</div>
							</div>

							<div class="form-group row">
								<label class="col-md-2 control-label">区（县）</label>
								<input type="hidden" name="districtName" v-model="m.districtName" />
								<input type="hidden" name="districtId" v-model="m.districtId" />
								<div class="col-md-3">
									<select v-model="districtNameList" class="bs-select form-control" >
										<option disabled selected value>请选择</option>
										<option  v-for="district in districtNameLists" value="{{[district.districtName,district.id]}}" v-if="district.cityId == cityNameList.split(',')[1]">
											{{district.districtName}}
										</option>
									</select>
								</div>

								<label  class="col-md-2 control-label">描述</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="description" v-model="m.description">
								</div>
							</div>
							<div class="form-group row">
								<label class="col-md-2 control-label">是否启用<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
								   是<input name="state" type="radio"  v-model="m.state"  :value="0">   否<input name="state" type="radio"  v-model="m.state"  :value="1">
								</div>

								<label class="col-md-2 control-label">原材编码</label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="materialCode" v-model="m.materialCode" disabled
										   :readonly="materReadonly" >
								</div>
							</div>


						</div>
						<div class="form-group text-center">
							<button class="btn green"  @click="save">保存</button>
							<a class="btn default" @click="cancel" >取消</a>
						</div>
				</div>
			</div>
		</div>
	</div>
		<%--新建原料结束--%>

		<%--新建品牌开始--%>

		<div class="row form-div" v-show="showCategoryForm" style="z-index:100;">
			<div class="col-md-offset-3 col-md-6" style="text-align:center">
				<div class="portlet light bordered">
					<div class="portlet-title">
						<div class="caption">
							<span class="caption-subject bold font-blue-hoki">新增品牌</span>
						</div>
					</div>
					<div class="portlet-body">
						<div class="form-body">
							<div class="form-group row" >
								<label class="col-md-4 control-label">分类名称<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="categoryName" v-model="parameter.categoryName" required="required">
								</div>
							</div>

							<div class="form-group row">
								<label class="col-md-4 control-label">一级分类<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select class="bs-select form-control" name="categoryOneId" v-model="parameter.categoryOneId" >
										<option disabled="" selected="" value="">请选择</option>
										<option  v-for="categoryOne in categoryOnes" :value="categoryOne.id">
											{{categoryOne.categoryName}}
										</option>
									</select>
								</div>
							</div>
							<div class="form-group row">
								<label class="col-md-4 control-label">二级分类<span style="color:#FF0000;">*</span></label>
								<div class="col-md-3">
									<select name="parentId" v-model="parameter.parentId" class="bs-select form-control" >
										<option disabled="" selected="" value="">请选择</option>
										<%--v-if 和 v-model 冲突--%>
										<%--<option  v-for="categoryTwo in categoryTwos" :value="categoryTwo.id" v-if="parameter.categoryOneId == categoryTwo.parentId">--%>
											<%--{{categoryTwo.categoryName}}--{{categoryTwo.id}}--%>
										<%--</option>--%>
										<option  v-for="categoryTwo in testList" :value="categoryTwo.id">
											{{categoryTwo.categoryName}}
										</option>

									</select>
								</div>
							</div>
							<div class="form-group row" >
								<label class="col-md-4 control-label">排序</label>
								<div class="col-md-3">
									<input type="number" class="form-control" name="sort" v-model="parameter.sort"  required="required">
								</div>
							</div>
							<div class="form-group row" >
								<label class="col-md-4 control-label" >关键词</label>
								<div class="col-md-3">
									<input type="text" class="form-control" name="keyword" v-model="parameter.keyword"  required="required">
								</div>
							</div>
						</div>
						<div class="form-group row">
							<label class="col-md-2 control-label" >备注</label>
							<div class="col-sm-8">
								<textarea class="form-control" name="categoryDesc" v-model="parameter.categoryDesc"></textarea>
							</div>
						</div>
						<div class="form-group text-center">
							<input class="btn green"  type="submit" @click="saveThirdCategory"  value="提交"/>&nbsp;&nbsp;&nbsp;
							<a class="btn default" @click="detailsCl">取消</a>
						</div>
					</div>
				</div>
			</div>
			<!--新增分类结束-->
		</div>

		<%--新建品牌结束--%>
		<div class="table-div">
			<div class="table-operator">
				<s:hasPermission name="scmMaterial/add">
					<button class="btn green pull-right" @click="create">新建</button>
				</s:hasPermission>
			</div>
			<div class="clearfix"></div>
			<div class="table-filter"></div>
			<div class="table-body">
				<table class="table table-striped table-hover table-bordered "></table>
			</div>
		</div>

    </div>


<script>
    (function(){
        var $table = $(".table-body>table");
        var allArticles = [];
        var tb = $table.DataTable({
            ajax : {
                url : "scmMaterial/list_all",
                dataSrc : "data"
            },
            ordering: false,//取消上下排序
            columns : [
                {
                    title : "类型",
                    data : "materialType",
                    createdCell:function (td,tdData) {//td中的数据
                        switch (tdData){
							case 'INGREDIENTS':tdData='主料';break;
                            case 'ACCESSORIES':tdData='辅料';break;
                            case 'SEASONING':tdData='配料';break;
                            case 'MATERIEL':tdData='物料';break;
						}
                        $(td).html(tdData);
                    },
                    s_filter: true,//标题中select--value和内容相同
                    s_render: function (tdData) {//标题中select--value中的数据
                        switch (tdData){
                            case 'INGREDIENTS':tdData='主料';break;
                            case 'ACCESSORIES':tdData='辅料';break;
                            case 'SEASONING':tdData='配料';break;
                            case 'MATERIEL':tdData='物料';break;

                        }
                        return tdData;
                    }
                },
                {
                    title : "一级类别",
                    data : "categoryOneName",
                    s_filter: true,
                },
                {
                    title : "二级类别",
                    data : "categoryTwoName",
                    s_filter: true,//标题中select--value和内容相同
                }
                ,
                {
                    title : "品牌",
                    data : "categoryThirdName",
                    s_filter: true,
                },
                {
                    title : "材料名",
                    data : "materialName",
                },
                {
                    title : "材料别名",
                    data : "printName",
                },
				{
                    title : "是否启用",
                    data : "state",
                    createdCell:function (td,tdData) {//td中的数据
                        switch (tdData){
                            case 0:tdData='已启用';break;
                            case 1:tdData='未启用';break;
                        }
                        $(td).html(tdData);
                    },
                },
//                {
//                    title : "序号 ",
//                    data : "priority",
//                },
                {
                    title : "编码",
                    data : "materialCode",
                },
                {
                    title : "规格",
                    data : "specName",
                    createdCell:function (td,tdData,rowData) {
                        $(td).html(rowData.measureUnit+rowData.unitName+'/'+rowData.specName);
                    }
                },
                {
                    title : "转化规格",
                    data : "rate",
                },
                {
                    title : "转换单位",
                    data : "convertUnitName",
                },
				{
                    title : "最小单位",
                    data : "minMeasureUnit",
                    createdCell:function (td,tdData,rowData) {
                        $(td).html(tdData+rowData.convertUnitName);
                    }
                },
                {
                    title : "最小单位份数",
                    data : "coefficient",
                },
                {
                    title : "产地",
                    data : "provinceName",
                    createdCell:function (td,tdData,rowData) {
                        if (tdData === null || tdData === "" || tdData === ''){
                            $(td).html('');
						}else {
                            $(td).html(tdData+rowData.cityName+rowData.districtName);
						}
                    }
                },

                {
                    title : "描述",
                    data : "description",
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        var operator=[
                            <s:hasPermission name="scmMaterial/delete">
                            C.createDelBtn(tdData,"scmMaterial/delete"),
                            </s:hasPermission>
                            <s:hasPermission name="scmMaterial/edit">
                            C.createEditBtn(rowData),
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }],
            initComplete: function () {
                var api = this.api();
                api.search('');
                var data = api.data();
                for (var i = 0; i < data.length; i++) {
                    allArticles.push(data[i]);
                }

                var columnsSetting = api.settings()[0].oInit.columns;
                $(columnsSetting).each(function (i) {
                    if (this.s_filter) {
                        var column = api.column(i);
                        var title = this.title;
                        var select = $('<select><option value="">' + this.title + '(全部)</option></select>');
                        var that = this;
                        column.data().unique().each(function (d) {
                            select.append('<option value="' + d + '">' + ((that.s_render && that.s_render(d)) || d) + '</option>')
                        });

                        select.appendTo($(column.header()).empty()).on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                $(this).val()
                            );
                            column.search(val ? '^' + val + '$' : '', true, false).draw();
                        });
                    }
                });
            }
        });
        var C = new Controller(null,tb);
        var vueObj = new Vue({
            mixins:[C.formVueMix],
            el:"#control",
            data:{
                materialTypes: [ //类型
                    {code:"INGREDIENTS" , name:"主料"},
                    {code:"ACCESSORIES" , name:"辅料"},
                    {code:"SEASONING" ,name:"配料"},
                    {code:"MATERIEL" ,name:"物料"},
                ],
                allArticles: allArticles,
                categoryOnes:[],//一级类别集合
                categoryTwos:[],//二级类别集合
                categoryThirds:[],//品牌类别集合
                specLists:[],//规格
                specList:[],//规格
                unitLists:[],//各种单位
                unitList:[],//各种单位
                convertUnitLists:[],//转换单位
                convertUnitList:[],//转换单位
                provinceNameLists:[],//省份
                provinceNameList:'',//接收省份
                cityNameLists:[],//市
                cityNameList:'',//接收市
                districtNameLists:[],//区
                districtNameList:'',//接收区
                coefficients:'',//计算的值
                materReadonly:false,//编码
                showCategoryForm:false,

                m:{
                    materialType: '',
                    categoryOneId :'',
                    categoryTwoId :'',
                    categoryThirdId :'',
                    materialName :'',
                    materialCode :'',
                    specId:'',//规格
                    specName:'',//规格
                    priority:'',
                    unitId:'',//标准单位
                    unitName:'',//标准单位
                    convertUnitId :'',//转换单位
                    convertUnitName:'',//转换单位
                    minConvertUnitId  :'',
                    provinceId :'',//省id
                    provinceName :'',//省名
                    cityId:'',//市id
                    cityName:'',//市id
                    districtId :'',
                    description :'',
                    minMeasureUnit:'',
                    coefficient:'',
                    state:'',//状态 0-未启用 1-启用
                    printName:'',//原料别名
                },
            },
          //过滤二级分类不可以绑定
            computed:{
				testList : function () {
				    var that = this;
					return this.categoryTwos.filter(function (c) {
					    //js原生语法过滤 c是自己
						return that.parameter.categoryOneId == c.parentId;
                    });
                }
			},
            watch:{ //监控事件
                provinceNameList:function (a,b) {//监控省
                    var provinces=a.split(',');
                    this.m.provinceName=provinces[0];
                    this.m.provinceId=parseInt(provinces[1]);
                },
                cityNameList:function (a,b) {//监控市
                    var provinces=a.split(',')
                    this.m.cityName=provinces[0];
                    this.m.cityId=provinces[1];
                },
                districtNameList:function (a,b) {//监控区
                    var provinces=a.split(',')
                    this.m.districtName=provinces[0];
                    this.m.districtId=provinces[1];
                },

                specList:function (a,b) {//规格
                    var provinces=a.split(',')
                    this.m.specName=provinces[0];
                    this.m.specId=provinces[1];
                },
                unitList:function (a,b) {//标准单位
                    var provinces=a.split(',')
                    this.m.unitName=provinces[0];
                    this.m.unitId=provinces[1];
                },
                convertUnitList:function (a,b) {//转换单位
                    var provinces=a.split(',')
                    this.m.convertUnitName=provinces[0];
                    this.m.convertUnitId=provinces[1];
                },
            },
            methods:{
                openForm:function(){
                    this.showform = true;
                },

                closeForm:function(){
                    this.m={};
                    this.showform = false;
                },
                cancel:function(){
                    this.m={};
                    this.closeForm();
                },
                categoryOneIdCh:function () {//点击监控一级类别
                    this.m.categoryTwoId='';
                    this.m.categoryThirdId='';
                },
                categoryTwoIdCh:function () {//点击监控二级类别
                    this.m.categoryThirdId='';
                },
                provinceNameListCh:function () {//点击监控省份
                    this.cityNameList='';
                    this.districtNameList='';
                },
                cityNameListCh:function () {//点击监控市区
                    this.districtNameList='';
                },
                com:function () { //计算系数
                    var Mcoefficient=0;
                    Mcoefficient=parseFloat(this.m.measureUnit)*parseFloat(this.m.rate)/parseFloat(this.m.minMeasureUnit);
                    if(Mcoefficient){
                        this.coefficients=Mcoefficient;
                    }
                },

                create:function(){//新增
                    this.materReadonly=false;
                    this.m={
                        materialType: '',
                        categoryOneId :'',
                        categoryTwoId :'',
                        categoryThirdId :'',
                        materialName :'',
                        materialCode :'',
                        specId:'',//规格
                        specName:'',//规格
                        priority:'1',
                        unitId:'',//标准单位
                        unitName:'',//标准单位
                        convertUnitId :'',//转换单位
                        convertUnitName:'',//转换单位
                        minConvertUnitId  :'',
                        provinceId :'',//省id
                        provinceName :'',//省名
                        cityId:'',//市id
                        cityName:'',//市id
                        districtId :'',
                        description :'',
                        minMeasureUnit:'',
                        coefficient:'',
                        state:'',//状态 0-未启用 1-启用
                        printName:'',//原料别名
                    };
                    this.provinceNameList='';//绑定省
                    this.cityNameList='';//绑定市
                    this.districtNameList='';//绑定区
                    this.specList='';//规格
                    this.unitList='';//标准单位
                    this.convertUnitList='';//转换单位
                    this.showform=true;

                },
                edit:function(model){//编辑
                    this.m= model;
                    this.m={
                        id:model.id,
                        materialType: model.materialType,
						categoryOneId :model.categoryOneId,
						categoryTwoId :model.categoryTwoId,
						categoryThirdId :model.categoryThirdId,
						materialName :model.materialName,
						materialCode :model.materialCode,
						specId:model.specId,//规格
						specName:model.specName,//规格
						priority:model.priority,
						unitId:model.unitId,//标准单位
						unitName:model.unitName,//标准单位
						convertUnitId :model.convertUnitId,//转换单位
						convertUnitName:model.convertUnitName,//转换单位
						minConvertUnitId:model.minConvertUnitId,
						provinceId :model.provinceId?model.provinceId:'',//省id
						provinceName:model.provinceName,//省名
						cityId:model.cityId?model.cityId:'',//市id
						cityName:model.cityName,//市id
						districtId :model.districtId?model.districtId:'',//市id
                        districtName:model.districtName,//区
						description :model.description,
						minMeasureUnit:model.minMeasureUnit,
						coefficient:model.coefficient,
                        measureUnit:model.measureUnit,//规格的核算单位值
                        rate:model.rate,//转换率
                        state:model.state,//状态 0-未启用 1-启用
                        printName:model.printName,//原料别名
                    };
                    debugger
					if(model.provinceId==null){
						this.provinceNameList=''
					}else{
						this.provinceNameList=model.provinceName+','+model.provinceId;//绑定省
					}
					if(model.cityId==null){
						this.cityNameList=''
					}else{
						this.cityNameList=model.cityName+','+model.cityId;//绑定市
					}
					if(model.districtId==null){
						this.districtNameList=''
					}else{
						this.districtNameList=model.districtName+','+model.districtId;//绑定区
					}
                    this.specList=model.specName+','+model.specId;//规格
                    this.unitList=model.unitName+','+model.unitId;//标准单位
                    this.convertUnitList=model.convertUnitName+','+model.convertUnitId;//转换单位
                    this.openForm();
                },
                save:function(){
                    var submit=false;
                    var message='';
					if(!this.m.materialType) message='类型';
                    else if(!this.m.categoryOneId) message='一级类别';
                    else if(!this.m.categoryTwoId) message='二级类别';
                    else if(!this.m.categoryThirdId) message='品牌';
                    else if(!this.m.materialName) message='原材料名';
                    else if(!this.m.priority) message='序号';
                    else if(!this.m.specId) message='库存单位';
                    else if(!this.m.measureUnit) message='核算规格';
                    else if(!this.m.unitId) message='核算单位';
                    else if(!this.m.rate) message='转化规格';
                    else if(!this.m.convertUnitId) message='转化单位';
                    else if(!this.m.minMeasureUnit) message='最小单位';
                    else  submit=true;
                  debugger
                     if(!this.m.printName){
                         this.m.printName = this.m.materialName
					 }

                    if(this.m.state ='' ||!this.m.state){
                        this.m.state =0;
					}else{
                        this.m.state =1;
					}

                    if(submit){
                        if(this.m.id) C.systemButton('scmMaterial/modify',this.m,['编辑成功','编辑失败']);
                        else C.systemButton('scmMaterial/create',this.m,['新增成功','新增失败']);
                        this.showform=false;
					}else {
                        C.systemButtonNo('error','请填写'+message);
					}

                },
                categoryOneCh:function(){
                    this.parameter.parentId='';
                },
                addThirdCategory:function(){
                    var test = this.m.categoryTwoId;
                    this.showCategoryForm=true;
                    this.parameter={
                        categoryName:'',//分类名称
                        parentId:this.m.categoryTwoId,//ID
                        sort:'1',//排序
                        keyword:'',//关键词
                        categoryOneId:this.m.categoryOneId,
                        categoryDesc:"",//备注
                        categoryHierarchy:'3',
                    };

				},
                detailsCl:function () {//取消新增
                    this.showCategoryForm=false;
                },
                saveThirdCategory:function(){//新增分类提交
                    var that=this;
                    var submit=false;
                    var message='';
                    if(!this.parameter.categoryName) message='分类名称';
                    else if(!this.parameter.categoryOneId) message='一级类别';
                    else if(!this.parameter.parentId) message='二级类别';
                    else submit=true;
                    if(submit){
                        delete this.parameter.categoryOneId;
                        $.ajax({
                            type:"POST",
                            url:'scmCategory/create',
                            contentType:"application/json",
                            datatype: "json",
                            data:JSON.stringify(that.parameter),
                            beforeSend:function(){ //请求之前执行
                            },
                            success:function(data){ //成功后返回
                                C.systemButtonNo('success','成功');
                                that.showCategoryForm=false;

                                $.post("scmCategory/list_categoryHierarchy?categoryHierarchy=3", null, function (data) {
                                    that.categoryThirds = data.data;
                                });

                            },
                            error: function(){ //失败后执行
                                C.systemButtonNo('error','失败');
                            }
                        });
                    }else {
                        C.systemButtonNo('error','请填写'+message);
                    }
                }


            },
            ready:function () { //钩子函数
                var that = this;
                $.post("scmUnit/list_type?type=1", null, function (data) {
                    that.unitLists = data.data;
                    that.convertUnitLists = data.data;
                });

                $.post("scmUnit/list_type?type=2", null, function (data) {
                    that.specLists = data.data;
                });
                $.post("scmCategory/list_categoryHierarchy?categoryHierarchy=1", null, function (data) {
                    that.categoryOnes = data.data;
                });
                $.post("scmCategory/list_categoryHierarchy?categoryHierarchy=2", null, function (data) {
                    that.categoryTwos = data.data;
                });

                $.post("scmCategory/list_categoryHierarchy?categoryHierarchy=3", null, function (data) {
                    that.categoryThirds = data.data;
                });

                $.post("province/list_province", null, function (data) {
                    that.provinceNameLists = data;
                });

                $.post("province/list_city", null, function (data) {
                    that.cityNameLists = data;
                });

                $.post("province/list_district", null, function (data) {
                    that.districtNameLists = data;
                });
            },

        });
        C.vue=vueObj;
    }());

</script>
