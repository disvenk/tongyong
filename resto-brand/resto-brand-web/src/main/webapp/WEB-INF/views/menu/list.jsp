<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="../tag-head.jsp" %>

<div class="row" id="permission-manager-div">
	<div class="col-md-12 margin-bottom-20">
		<div class="btn-group" data-toggle="buttons">
			<c:forEach items="${usergroup }" var="g" varStatus="state"> 
			  <label class="btn btn-default margin-right-10 user-group-btn ${state.first?'active':'' }" 
			  @click="changeGroupId('${g.id }')">
		           	<input type="radio" class="toggle" value="${g.id }"> ${g.name }
		           	<i class="fa fa-edit edit-user-group" data-id="${g.id }" data-name="${g.name }" @click.stop="editusergroup"></i>
		           	<i class="fa fa-close delete-user-group" data-id="${g.id }" @click.stop="delusergroup"></i>
		           	 </label>
			</c:forEach>
	    </div>
	    <button class="btn green" @click="addusergroup">新增用户组</button>
	</div>
	<div class="col-md-4">
		<div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption font-red-sunglo">
                    <i class="icon-settings font-red-sunglo"></i>
                    <span class="caption-subject bold"> 权限列表</span>
                </div>
                <div class="actions">
                	<button class="btn btn-danger" @click="addPermission">新增</button>
	             </div>
            </div>
            <div class="portlet-body">
                <div id="assign-tree">正在加载权限列表.....</div>
            </div>
        </div>
	</div>
	<div class="col-md-8">
		<div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption font-red-sunglo">
                    <i class="icon-settings font-red-sunglo"></i>
                    <span class="caption-subject bold" v-if="node.id">                 	
                    	编辑:{{node.text}}
                    </span>
                    <span class="caption-subject bold" v-else>                 	
                    	新增权限
                    </span>
                </div>
            </div>
            <div class="portlet-body form">
            	<form role="form" action="menu/save" id="save-permission-form" @submit.prevent="savepermission">
					<input type="hidden" name="id" v-model="c.id">
					<input type="hidden" name="isMenu" value="true"/>
					<input type="hidden" name="userGroupId" v-model="userGroupId"/>
					<div class="form-body">
						<div class="form-group"  v-if="node.parent!='#'&&permissions.length>0">
							<label>父菜单</label> 
							<select class="form-control" name="parentId" v-model="parentId">
								<option v-for="p in permissions" :value="p.id">{{p.text}}</option>
							</select>
						</div>
						<div class="form-group">
							<label>图标</label>
							<div class="input-group">
								<span class="input-group-addon">
									<i class="fa {{c.menuIcon}}"></i>
								</span>
								<input type="text" class="form-control" name="menuIcon" v-model="c.menuIcon">
							</div> 
						</div>
						<div class="form-group">
							<label>菜单名称</label>
							<input type="text" class="form-control" name="permissionName" v-model="c.permissionName">
						</div>
						<div class="form-group">
							<label>菜单地址</label>
							<input class="form-control" name="permissionSign"  v-model="c.permissionSign"/>
						</div>
						<div class="form-group"  v-if="node.parent!='#'">
							<label>菜单类型</label> 
							<select class="form-control" name="menuType" v-model="c.menuType">
								<option value="1">Ajax</option>
								<option value="2">Iframe</option>
								<option value="3">Link(Open in new window)</option>
								<option value="4">Button</option>
							</select>
						</div>
						<div class="form-group">
							<label>菜单排序</label>
							<input type="text" class="form-control" name="sort" v-model="c.sort">
						</div>
						<div class="form-group"  v-if="node.parent!='#'&&node.id">
							<label>子权限</label>
							<div>
								<a href="javascript:;" class="btn red margin-right-10" @click="editSubPermission(p.id)" v-for="p in c.subPermission" :title="p.permissionSign">
										{{p.permissionName}}
									<i class="fa fa-close" @click.stop="delPermission(p.id)"></i></a>
								<a href="javascript:;" class="btn blue margin-right-10" @click="addSubPermission(c.id)">
									<i class="fa fa-plus"></i>	
									添加按钮权限
								</a>
							</div>
						</div>
						<div class="form-actions">
                            <button type="submit" class="btn blue">Submit</button>
                        </div>
					</div>
				</form>
            </div>
        </div>
	</div>
</div>

<script>
	
	var PermissionManager = function(){
		var C = new Controller();
		this.app = new Vue({
			el:"#permission-manager-div",
			data:{
				userGroupId:1,
				permissions:[],
				allUrls:[],
				c:{},  //当前权限
				node:{}, //当前节点
				parentId:'#'
			},
			methods:{
				addPermission:function(pid){
					this.node={};
					this.parentId=pid;
					this.c.menuIcon="fa bar";
					$("[name='permissionName']").focus();
				},
				addSubPermission:function(pid){
					var gid = this.userGroupId;
					C.loadForm({
						url:"menu/addSub",
						formaction:"menu/addSubData",
						data:{parentId:pid},
						aftersuccess:function(){
							loadAllPermission();
						}
					});
				},
				editSubPermission:function(id){
					C.loadForm({
						url:"menu/editSub",
						formaction:"menu/editSubData",
						data:{id:id},
						aftersuccess:function(){
							loadAllPermission();
						}
					});
				},
				delPermission:function(pid){
					deleteById(pid);
					return false;
				},
				changeGroupId :function(id){
					this.userGroupId=id;
					loadAllPermission();
				},
				savepermission:function(e){
					var that = this;
					var formDom = e.target;
					C.ajaxFormEx(formDom,loadAllPermission);
				},
				addusergroup:function(){
					loadGroupForm();
				},
				delusergroup:function(e){
					var id = $(e.target).data("id");
					loadGroupDel(id);
				},
				editusergroup:function(e){
					var id = $(e.target).data("id");
					var name = $(e.target).data("name");
					loadGroupForm(id,name);
				}
				
			}
		});
		
		app.$watch("node",function(node){
			if(!node.id){
				this.c={};
				deselectTree();
			}else{
				this.parentId = node.parent;
				loadPermissionById(node.id);
			}
		});
		
		this.init = function(){
			loadAllPermission();
			hadnlerIconsQuery();
		}
		
		this.handlerUrlsQuery = function(){	
			var numbers = new Bloodhound({
	          datumTokenizer: Bloodhound.tokenizers.nonword,
	          queryTokenizer: Bloodhound.tokenizers.nonword,
	          local:app.allUrls
	        });
			 numbers.initialize();
			 $('[name="permissionSign"]').typeahead(null, {
				 highlight:true,
	             source: numbers.ttAdapter(),
	         });
			 
			 C.handlerFormAjax($("#save-permission-form"),{aftersuccess:function(){
				 loadAllPermission();
			 }});
		}
		
		this.hadnlerIconsQuery = function(){
			var icons = new Bloodhound({
		          datumTokenizer: Bloodhound.tokenizers.nonword,
		          queryTokenizer: Bloodhound.tokenizers.nonword,
		          local:iconAllArray
		        });
			 
			 $('[name="menuIcon"]').typeahead(null, {
	             minLength:50,
	             source: icons.ttAdapter(),
	         });
			 
			 $('[name="menuIcon"]').on("typeahead:render",function(){
				$(this).parents(".twitter-typeahead").find(".tt-suggestion.tt-selectable").each(function(){
					$(this).addClass($(this).html());
				});
			 });
		}
		
		this.loadAllUrls = function(){
			handlerUrlsQuery();
// 			C.ajax("menu/allUrls",null,function(result){
// 				app.allUrls = result;
// 				handlerUrlsQuery(result);
// 			});
		}
		this.loadAllPermission = function(){
			C.ajax("menu/allAssignJsTreeData",{userGroupId:app.userGroupId},function(result){
				app.permissions=result;
				loadTree(result);
			});
		}
		
		this.loadPermissionById = function(id){
			C.ajax("menu/findById",{id:id},function(result){
				app.c=result;
				if(app.parentId!='#'){
					C.ajax("menu/listData",{parentId:app.c.id},function(subPermission){
						app.$set("c.subPermission",subPermission);
					});
				}
			});
		}
		
		this.deleteById = function(id){
			C.confirmDialog("你确定要删除吗？","提示",function(){
				C.ajax("menu/delete",{id:id},function(result){
					if(result.success){
						loadAllPermission();
						app.node={};
					}else{
						C.errorMsg(result.message);
					}
				});
			});
		}
		
		this.loadTree = function (allPermission){
			$('#assign-tree').jstree();
			$('#assign-tree').jstree().destroy();
			$('#assign-tree').jstree({
				'plugins' : [ "wholerow",  "types","contextmenu","state"],
				'core' : {
					"themes" : {
						"responsive" : false
					},
					'data' : allPermission
				},
				"types" : {
					"default" : {
						"icon" : "fa fa-folder icon-state-warning icon-lg"
					},
					"file" : {
						"icon" : "fa fa-file icon-state-warning icon-lg"
					}
				},
				"contextmenu":{
					items:function(node){
						var create = {
								"separator_before"	: false,
								"separator_after"	: true,
								"_disabled"			: false, //(this.check("create_node", data.reference, {}, "last")),
								"label"				: "创建子权限",
								"action"			: function (data) {
									app.addPermission(node.id);
								}
							};
						var delItem ={
								"separator_before"	: true,
								"separator_after"	: false,
								"_disabled"			: false, //(this.check("create_node", data.reference, {}, "last")),
								"label"				: "删除权限",
								"action"			: function (data) {
									deleteById(node.id);
								}
							};
						
						var items = {"create":create,"del":delItem};
							
						if(node.parents.length!=1){
							delete items.create;
						}
						return items;
					}
				}
			});
			
			$('#assign-tree').on("select_node.jstree",function(event,target){
				var node = target.node;
				app.node=node;
			});
		}
		
		function deselectTree(){
			$('#assign-tree').jstree().deselect_all();
		}
		
		function loadGroupDel(id){
			C.delConfirmDialog(function(){
				C.ajax("usergroup/delete",{id:id},function(){
					$("#menu-manager").click();
				});
			})
		}
		
		function loadGroupForm(id,name){
			var form = $("<form>"+
			"ID  :<input type='text' name='id' class='form-control'/>"+
			"NAME: <input type='text' name='name' class='form-control'/></form>");
			var action = "usergroup/create";
			if(id){
				form.find("[name='name']").val(name);
				form.find("[name='id']").attr("readonly","readonly").val(id);
				action = "usergroup/modify";
			}
			C.loadForm({
				title:"请输入用户组名称",
				formDom:form,
				formaction:action,
				aftersuccess:function(){
					console.log("success");
					$("#menu-manager").click();
				}
			});
		}
		return this;
	}();
	
	
	PermissionManager.init();

	
	
	//图标库
	var iconAllArray = [ 'fa fa-plus', 'fa fa-bolt', 'fa fa-bell-o',
			'fa fa-bullhorn', 'fa fa-bolt', 'fa fa-bolt', 'fa fa-bell-o',
			'fa fa-bullhorn', 'fa fa-bolt', 'fa fa-angle-down', 'fa fa-circle',
			'fa fa-angle-down', 'fa fa-500px', 'fa fa-amazon',
			'fa fa-balance-scale', 'fa fa-battery-0', 'fa fa-battery-1',
			'fa fa-battery-2', 'fa fa-battery-3', 'fa fa-battery-4',
			'fa fa-battery-empty', 'fa fa-battery-full', 'fa fa-battery-half',
			'fa fa-battery-quarter', 'fa fa-battery-three-quarters',
			'fa fa-black-tie', 'fa fa-calendar-check-o',
			'fa fa-calendar-minus-o', 'fa fa-calendar-plus-o',
			'fa fa-calendar-times-o', 'fa fa-cc-diners-club', 'fa fa-cc-jcb',
			'fa fa-chrome', 'fa fa-clone', 'fa fa-commenting',
			'fa fa-commenting-o', 'fa fa-contao', 'fa fa-creative-commons',
			'fa fa-expeditedssl', 'fa fa-firefox', 'fa fa-fonticons',
			'fa fa-genderless', 'fa fa-get-pocket', 'fa fa-gg',
			'fa fa-gg-circle', 'fa fa-hand-grab-o', 'fa fa-hand-lizard-o',
			'fa fa-hand-paper-o', 'fa fa-hand-peace-o', 'fa fa-hand-pointer-o',
			'fa fa-hand-rock-o', 'fa fa-hand-scissors-o', 'fa fa-hand-spock-o',
			'fa fa-hand-stop-o', 'fa fa-hourglass', 'fa fa-hourglass-1',
			'fa fa-hourglass-2', 'fa fa-hourglass-3', 'fa fa-hourglass-end',
			'fa fa-hourglass-half', 'fa fa-hourglass-o',
			'fa fa-hourglass-start', 'fa fa-houzz', 'fa fa-i-cursor',
			'fa fa-industry', 'fa fa-internet-explorer', 'fa fa-map',
			'fa fa-map-o', 'fa fa-map-pin', 'fa fa-map-signs',
			'fa fa-mouse-pointer', 'fa fa-object-group',
			'fa fa-object-ungroup', 'fa fa-odnoklassniki',
			'fa fa-odnoklassniki-square', 'fa fa-opencart', 'fa fa-opera',
			'fa fa-optin-monster', 'fa fa-registered', 'fa fa-safari',
			'fa fa-sticky-note', 'fa fa-sticky-note-o', 'fa fa-television',
			'fa fa-trademark', 'fa fa-tripadvisor', 'fa fa-tv', 'fa fa-vimeo',
			'fa fa-wikipedia-w', 'fa fa-y-combinator', 'fa fa-yc',
			'fa fa-adjust', 'fa fa-anchor', 'fa fa-archive',
			'fa fa-area-chart', 'fa fa-arrows', 'fa fa-arrows-h',
			'fa fa-arrows-v', 'fa fa-asterisk', 'fa fa-at', 'fa fa-automobile',
			'fa fa-balance-scale', 'fa fa-ban', 'fa fa-bank',
			'fa fa-bar-chart', 'fa fa-bar-chart-o', 'fa fa-barcode',
			'fa fa-bars', 'fa fa-battery-0', 'fa fa-battery-1',
			'fa fa-battery-2', 'fa fa-battery-3', 'fa fa-battery-4',
			'fa fa-battery-empty', 'fa fa-battery-full', 'fa fa-battery-half',
			'fa fa-battery-quarter', 'fa fa-battery-three-quarters',
			'fa fa-bed', 'fa fa-beer', 'fa fa-bell', 'fa fa-bell-o',
			'fa fa-bell-slash', 'fa fa-bell-slash-o', 'fa fa-bicycle',
			'fa fa-binoculars', 'fa fa-birthday-cake', 'fa fa-bolt',
			'fa fa-bomb', 'fa fa-book', 'fa fa-bookmark', 'fa fa-bookmark-o',
			'fa fa-briefcase', 'fa fa-bug', 'fa fa-building',
			'fa fa-building-o', 'fa fa-bullhorn', 'fa fa-bullseye',
			'fa fa-bus', 'fa fa-cab', 'fa fa-calculator', 'fa fa-calendar',
			'fa fa-calendar-check-o', 'fa fa-calendar-minus-o',
			'fa fa-calendar-o', 'fa fa-calendar-plus-o',
			'fa fa-calendar-times-o', 'fa fa-camera', 'fa fa-camera-retro',
			'fa fa-car', 'fa fa-caret-square-o-down',
			'fa fa-caret-square-o-left', 'fa fa-caret-square-o-right',
			'fa fa-caret-square-o-up', 'fa fa-cart-arrow-down',
			'fa fa-cart-plus', 'fa fa-cc', 'fa fa-certificate', 'fa fa-check',
			'fa fa-check-circle', 'fa fa-check-circle-o', 'fa fa-check-square',
			'fa fa-check-square-o', 'fa fa-child', 'fa fa-circle',
			'fa fa-circle-o', 'fa fa-circle-o-notch', 'fa fa-circle-thin',
			'fa fa-clock-o', 'fa fa-clone', 'fa fa-close', 'fa fa-cloud',
			'fa fa-cloud-download', 'fa fa-cloud-upload', 'fa fa-code',
			'fa fa-code-fork', 'fa fa-coffee', 'fa fa-cog', 'fa fa-cogs',
			'fa fa-comment', 'fa fa-comment-o', 'fa fa-commenting',
			'fa fa-commenting-o', 'fa fa-comments', 'fa fa-comments-o',
			'fa fa-compass', 'fa fa-copyright', 'fa fa-creative-commons',
			'fa fa-credit-card', 'fa fa-crop', 'fa fa-crosshairs',
			'fa fa-cube', 'fa fa-cubes', 'fa fa-cutlery', 'fa fa-dashboard',
			'fa fa-database', 'fa fa-desktop', 'fa fa-diamond',
			'fa fa-dot-circle-o', 'fa fa-download', 'fa fa-edit',
			'fa fa-ellipsis-h', 'fa fa-ellipsis-v', 'fa fa-envelope',
			'fa fa-envelope-o', 'fa fa-envelope-square', 'fa fa-eraser',
			'fa fa-exchange', 'fa fa-exclamation', 'fa fa-exclamation-circle',
			'fa fa-exclamation-triangle', 'fa fa-external-link',
			'fa fa-external-link-square', 'fa fa-eye', 'fa fa-eye-slash',
			'fa fa-eyedropper', 'fa fa-fax', 'fa fa-feed', 'fa fa-female',
			'fa fa-fighter-jet', 'fa fa-file-archive-o', 'fa fa-file-audio-o',
			'fa fa-file-code-o', 'fa fa-file-excel-o', 'fa fa-file-image-o',
			'fa fa-file-movie-o', 'fa fa-file-pdf-o', 'fa fa-file-photo-o',
			'fa fa-file-picture-o', 'fa fa-file-powerpoint-o',
			'fa fa-file-sound-o', 'fa fa-file-video-o', 'fa fa-file-word-o',
			'fa fa-file-zip-o', 'fa fa-film', 'fa fa-filter', 'fa fa-fire',
			'fa fa-fire-extinguisher', 'fa fa-flag', 'fa fa-flag-checkered',
			'fa fa-flag-o', 'fa fa-flash', 'fa fa-flask', 'fa fa-folder',
			'fa fa-folder-o', 'fa fa-folder-open', 'fa fa-folder-open-o',
			'fa fa-frown-o', 'fa fa-futbol-o', 'fa fa-gamepad', 'fa fa-gavel',
			'fa fa-gear', 'fa fa-gears', 'fa fa-gift', 'fa fa-glass',
			'fa fa-globe', 'fa fa-graduation-cap', 'fa fa-group',
			'fa fa-hand-grab-o', 'fa fa-hand-lizard-o', 'fa fa-hand-paper-o',
			'fa fa-hand-peace-o', 'fa fa-hand-pointer-o', 'fa fa-hand-rock-o',
			'fa fa-hand-scissors-o', 'fa fa-hand-spock-o', 'fa fa-hand-stop-o',
			'fa fa-hdd-o', 'fa fa-headphones', 'fa fa-heart', 'fa fa-heart-o',
			'fa fa-heartbeat', 'fa fa-history', 'fa fa-home', 'fa fa-hotel',
			'fa fa-hourglass', 'fa fa-hourglass-1', 'fa fa-hourglass-2',
			'fa fa-hourglass-3', 'fa fa-hourglass-end', 'fa fa-hourglass-half',
			'fa fa-hourglass-o', 'fa fa-hourglass-start', 'fa fa-i-cursor',
			'fa fa-image', 'fa fa-inbox', 'fa fa-industry', 'fa fa-info',
			'fa fa-info-circle', 'fa fa-institution', 'fa fa-key',
			'fa fa-keyboard-o', 'fa fa-language', 'fa fa-laptop', 'fa fa-leaf',
			'fa fa-legal', 'fa fa-lemon-o', 'fa fa-level-down',
			'fa fa-level-up', 'fa fa-life-bouy', 'fa fa-life-buoy',
			'fa fa-life-ring', 'fa fa-life-saver', 'fa fa-lightbulb-o',
			'fa fa-line-chart', 'fa fa-location-arrow', 'fa fa-lock',
			'fa fa-magic', 'fa fa-magnet', 'fa fa-mail-forward',
			'fa fa-mail-reply', 'fa fa-mail-reply-all', 'fa fa-male',
			'fa fa-map', 'fa fa-map-marker', 'fa fa-map-o', 'fa fa-map-pin',
			'fa fa-map-signs', 'fa fa-meh-o', 'fa fa-microphone',
			'fa fa-microphone-slash', 'fa fa-minus', 'fa fa-minus-circle',
			'fa fa-minus-square', 'fa fa-minus-square-o', 'fa fa-mobile',
			'fa fa-mobile-phone', 'fa fa-money', 'fa fa-moon-o',
			'fa fa-mortar-board', 'fa fa-motorcycle', 'fa fa-mouse-pointer',
			'fa fa-music', 'fa fa-navicon', 'fa fa-newspaper-o',
			'fa fa-object-group', 'fa fa-object-ungroup', 'fa fa-paint-brush',
			'fa fa-paper-plane', 'fa fa-paper-plane-o', 'fa fa-paw',
			'fa fa-pencil', 'fa fa-pencil-square', 'fa fa-pencil-square-o',
			'fa fa-phone', 'fa fa-phone-square', 'fa fa-photo',
			'fa fa-picture-o', 'fa fa-pie-chart', 'fa fa-plane', 'fa fa-plug',
			'fa fa-plus', 'fa fa-plus-circle', 'fa fa-plus-square',
			'fa fa-plus-square-o', 'fa fa-power-off', 'fa fa-print',
			'fa fa-puzzle-piece', 'fa fa-qrcode', 'fa fa-question',
			'fa fa-question-circle', 'fa fa-quote-left', 'fa fa-quote-right',
			'fa fa-random', 'fa fa-recycle', 'fa fa-refresh',
			'fa fa-registered', 'fa fa-remove', 'fa fa-reorder', 'fa fa-reply',
			'fa fa-reply-all', 'fa fa-retweet', 'fa fa-road', 'fa fa-rocket',
			'fa fa-rss', 'fa fa-rss-square', 'fa fa-search',
			'fa fa-search-minus', 'fa fa-search-plus', 'fa fa-send',
			'fa fa-send-o', 'fa fa-server', 'fa fa-share', 'fa fa-share-alt',
			'fa fa-share-alt-square', 'fa fa-share-square',
			'fa fa-share-square-o', 'fa fa-shield', 'fa fa-ship',
			'fa fa-shopping-cart', 'fa fa-sign-in', 'fa fa-sign-out',
			'fa fa-signal', 'fa fa-sitemap', 'fa fa-sliders', 'fa fa-smile-o',
			'fa fa-soccer-ball-o', 'fa fa-sort', 'fa fa-sort-alpha-asc',
			'fa fa-sort-alpha-desc', 'fa fa-sort-amount-asc',
			'fa fa-sort-amount-desc', 'fa fa-sort-asc', 'fa fa-sort-desc',
			'fa fa-sort-down', 'fa fa-sort-numeric-asc',
			'fa fa-sort-numeric-desc', 'fa fa-sort-up', 'fa fa-space-shuttle',
			'fa fa-spinner', 'fa fa-spoon', 'fa fa-square', 'fa fa-square-o',
			'fa fa-star', 'fa fa-star-half', 'fa fa-star-half-empty',
			'fa fa-star-half-full', 'fa fa-star-half-o', 'fa fa-star-o',
			'fa fa-sticky-note', 'fa fa-sticky-note-o', 'fa fa-street-view',
			'fa fa-suitcase', 'fa fa-sun-o', 'fa fa-support', 'fa fa-tablet',
			'fa fa-tachometer', 'fa fa-tag', 'fa fa-tags', 'fa fa-tasks',
			'fa fa-taxi', 'fa fa-television', 'fa fa-terminal',
			'fa fa-thumb-tack', 'fa fa-thumbs-down', 'fa fa-thumbs-o-down',
			'fa fa-thumbs-o-up', 'fa fa-thumbs-up', 'fa fa-ticket',
			'fa fa-times', 'fa fa-times-circle', 'fa fa-times-circle-o',
			'fa fa-tint', 'fa fa-toggle-down', 'fa fa-toggle-left',
			'fa fa-toggle-off', 'fa fa-toggle-on', 'fa fa-toggle-right',
			'fa fa-toggle-up', 'fa fa-trademark', 'fa fa-trash',
			'fa fa-trash-o', 'fa fa-tree', 'fa fa-trophy', 'fa fa-truck',
			'fa fa-tty', 'fa fa-tv', 'fa fa-umbrella', 'fa fa-university',
			'fa fa-unlock', 'fa fa-unlock-alt', 'fa fa-unsorted',
			'fa fa-upload', 'fa fa-user', 'fa fa-user-plus',
			'fa fa-user-secret', 'fa fa-user-times', 'fa fa-users',
			'fa fa-video-camera', 'fa fa-volume-down', 'fa fa-volume-off',
			'fa fa-volume-up', 'fa fa-warning', 'fa fa-wheelchair',
			'fa fa-wifi', 'fa fa-wrench', 'fa fa-hand-grab-o',
			'fa fa-hand-lizard-o', 'fa fa-hand-o-down', 'fa fa-hand-o-left',
			'fa fa-hand-o-right', 'fa fa-hand-o-up', 'fa fa-hand-paper-o',
			'fa fa-hand-peace-o', 'fa fa-hand-pointer-o', 'fa fa-hand-rock-o',
			'fa fa-hand-scissors-o', 'fa fa-hand-spock-o', 'fa fa-hand-stop-o',
			'fa fa-thumbs-down', 'fa fa-thumbs-o-down', 'fa fa-thumbs-o-up',
			'fa fa-thumbs-up', 'fa fa-ambulance', 'fa fa-automobile',
			'fa fa-bicycle', 'fa fa-bus', 'fa fa-cab', 'fa fa-car',
			'fa fa-fighter-jet', 'fa fa-motorcycle', 'fa fa-plane',
			'fa fa-rocket', 'fa fa-ship', 'fa fa-space-shuttle',
			'fa fa-subway', 'fa fa-taxi', 'fa fa-train', 'fa fa-truck',
			'fa fa-wheelchair', 'fa fa-genderless', 'fa fa-intersex',
			'fa fa-mars', 'fa fa-mars-double', 'fa fa-mars-stroke',
			'fa fa-mars-stroke-h', 'fa fa-mars-stroke-v', 'fa fa-mercury',
			'fa fa-neuter', 'fa fa-transgender', 'fa fa-transgender-alt',
			'fa fa-venus', 'fa fa-venus-double', 'fa fa-venus-mars',
			'fa fa-file', 'fa fa-file-archive-o', 'fa fa-file-audio-o',
			'fa fa-file-code-o', 'fa fa-file-excel-o', 'fa fa-file-image-o',
			'fa fa-file-movie-o', 'fa fa-file-o', 'fa fa-file-pdf-o',
			'fa fa-file-photo-o', 'fa fa-file-picture-o',
			'fa fa-file-powerpoint-o', 'fa fa-file-sound-o', 'fa fa-file-text',
			'fa fa-file-text-o', 'fa fa-file-video-o', 'fa fa-file-word-o',
			'fa fa-file-zip-o', 'fa fa-info-circle fa-lg fa-li',
			'fa fa-circle-o-notch', 'fa fa-cog', 'fa fa-gear', 'fa fa-refresh',
			'fa fa-spinner', 'fa fa-check-square', 'fa fa-check-square-o',
			'fa fa-circle', 'fa fa-circle-o', 'fa fa-dot-circle-o',
			'fa fa-minus-square', 'fa fa-minus-square-o', 'fa fa-plus-square',
			'fa fa-plus-square-o', 'fa fa-square', 'fa fa-square-o',
			'fa fa-cc-amex', 'fa fa-cc-diners-club', 'fa fa-cc-discover',
			'fa fa-cc-jcb', 'fa fa-cc-mastercard', 'fa fa-cc-paypal',
			'fa fa-cc-stripe', 'fa fa-cc-visa', 'fa fa-credit-card',
			'fa fa-google-wallet', 'fa fa-paypal', 'fa fa-area-chart',
			'fa fa-bar-chart', 'fa fa-bar-chart-o', 'fa fa-line-chart',
			'fa fa-pie-chart', 'fa fa-bitcoin', 'fa fa-btc', 'fa fa-cny',
			'fa fa-dollar', 'fa fa-eur', 'fa fa-euro', 'fa fa-gbp', 'fa fa-gg',
			'fa fa-gg-circle', 'fa fa-ils', 'fa fa-inr', 'fa fa-jpy',
			'fa fa-krw', 'fa fa-money', 'fa fa-rmb', 'fa fa-rouble',
			'fa fa-rub', 'fa fa-ruble', 'fa fa-rupee', 'fa fa-shekel',
			'fa fa-sheqel', 'fa fa-try', 'fa fa-turkish-lira', 'fa fa-usd',
			'fa fa-won', 'fa fa-yen', 'fa fa-align-center',
			'fa fa-align-justify', 'fa fa-align-left', 'fa fa-align-right',
			'fa fa-bold', 'fa fa-chain', 'fa fa-chain-broken',
			'fa fa-clipboard', 'fa fa-columns', 'fa fa-copy', 'fa fa-cut',
			'fa fa-dedent', 'fa fa-eraser', 'fa fa-file', 'fa fa-file-o',
			'fa fa-file-text', 'fa fa-file-text-o', 'fa fa-files-o',
			'fa fa-floppy-o', 'fa fa-font', 'fa fa-header', 'fa fa-indent',
			'fa fa-italic', 'fa fa-link', 'fa fa-list', 'fa fa-list-alt',
			'fa fa-list-ol', 'fa fa-list-ul', 'fa fa-outdent',
			'fa fa-paperclip', 'fa fa-paragraph', 'fa fa-paste',
			'fa fa-repeat', 'fa fa-rotate-left', 'fa fa-rotate-right',
			'fa fa-save', 'fa fa-scissors', 'fa fa-strikethrough',
			'fa fa-subscript', 'fa fa-superscript', 'fa fa-table',
			'fa fa-text-height', 'fa fa-text-width', 'fa fa-th',
			'fa fa-th-large', 'fa fa-th-list', 'fa fa-underline', 'fa fa-undo',
			'fa fa-unlink', 'fa fa-angle-double-down',
			'fa fa-angle-double-left', 'fa fa-angle-double-right',
			'fa fa-angle-double-up', 'fa fa-angle-down', 'fa fa-angle-left',
			'fa fa-angle-right', 'fa fa-angle-up', 'fa fa-arrow-circle-down',
			'fa fa-arrow-circle-left', 'fa fa-arrow-circle-o-down',
			'fa fa-arrow-circle-o-left', 'fa fa-arrow-circle-o-right',
			'fa fa-arrow-circle-o-up', 'fa fa-arrow-circle-right',
			'fa fa-arrow-circle-up', 'fa fa-arrow-down', 'fa fa-arrow-left',
			'fa fa-arrow-right', 'fa fa-arrow-up', 'fa fa-arrows',
			'fa fa-arrows-alt', 'fa fa-arrows-h', 'fa fa-arrows-v',
			'fa fa-caret-down', 'fa fa-caret-left', 'fa fa-caret-right',
			'fa fa-caret-square-o-down', 'fa fa-caret-square-o-left',
			'fa fa-caret-square-o-right', 'fa fa-caret-square-o-up',
			'fa fa-caret-up', 'fa fa-chevron-circle-down',
			'fa fa-chevron-circle-left', 'fa fa-chevron-circle-right',
			'fa fa-chevron-circle-up', 'fa fa-chevron-down',
			'fa fa-chevron-left', 'fa fa-chevron-right', 'fa fa-chevron-up',
			'fa fa-exchange', 'fa fa-hand-o-down', 'fa fa-hand-o-left',
			'fa fa-hand-o-right', 'fa fa-hand-o-up', 'fa fa-long-arrow-down',
			'fa fa-long-arrow-left', 'fa fa-long-arrow-right',
			'fa fa-long-arrow-up', 'fa fa-toggle-down', 'fa fa-toggle-left',
			'fa fa-toggle-right', 'fa fa-toggle-up', 'fa fa-arrows-alt',
			'fa fa-backward', 'fa fa-compress', 'fa fa-eject', 'fa fa-expand',
			'fa fa-fast-backward', 'fa fa-fast-forward', 'fa fa-forward',
			'fa fa-pause', 'fa fa-play', 'fa fa-play-circle',
			'fa fa-play-circle-o', 'fa fa-random', 'fa fa-step-backward',
			'fa fa-step-forward', 'fa fa-stop', 'fa fa-youtube-play',
			'fa fa-warning', 'fa fa-500px', 'fa fa-adn', 'fa fa-amazon',
			'fa fa-android', 'fa fa-angellist', 'fa fa-apple', 'fa fa-behance',
			'fa fa-behance-square', 'fa fa-bitbucket',
			'fa fa-bitbucket-square', 'fa fa-bitcoin', 'fa fa-black-tie',
			'fa fa-btc', 'fa fa-buysellads', 'fa fa-cc-amex',
			'fa fa-cc-diners-club', 'fa fa-cc-discover', 'fa fa-cc-jcb',
			'fa fa-cc-mastercard', 'fa fa-cc-paypal', 'fa fa-cc-stripe',
			'fa fa-cc-visa', 'fa fa-chrome', 'fa fa-codepen',
			'fa fa-connectdevelop', 'fa fa-contao', 'fa fa-css3',
			'fa fa-dashcube', 'fa fa-delicious', 'fa fa-deviantart',
			'fa fa-digg', 'fa fa-dribbble', 'fa fa-dropbox', 'fa fa-drupal',
			'fa fa-empire', 'fa fa-expeditedssl', 'fa fa-facebook',
			'fa fa-facebook-f', 'fa fa-facebook-official',
			'fa fa-facebook-square', 'fa fa-firefox', 'fa fa-flickr',
			'fa fa-fonticons', 'fa fa-forumbee', 'fa fa-foursquare',
			'fa fa-ge', 'fa fa-get-pocket', 'fa fa-gg', 'fa fa-gg-circle',
			'fa fa-git', 'fa fa-git-square', 'fa fa-github',
			'fa fa-github-alt', 'fa fa-github-square', 'fa fa-gittip',
			'fa fa-google', 'fa fa-google-plus', 'fa fa-google-plus-square',
			'fa fa-google-wallet', 'fa fa-gratipay', 'fa fa-hacker-news',
			'fa fa-houzz', 'fa fa-html5', 'fa fa-instagram',
			'fa fa-internet-explorer', 'fa fa-ioxhost', 'fa fa-joomla',
			'fa fa-jsfiddle', 'fa fa-lastfm', 'fa fa-lastfm-square',
			'fa fa-leanpub', 'fa fa-linkedin', 'fa fa-linkedin-square',
			'fa fa-linux', 'fa fa-maxcdn', 'fa fa-meanpath', 'fa fa-medium',
			'fa fa-odnoklassniki', 'fa fa-odnoklassniki-square',
			'fa fa-opencart', 'fa fa-openid', 'fa fa-opera',
			'fa fa-optin-monster', 'fa fa-pagelines', 'fa fa-paypal',
			'fa fa-pied-piper', 'fa fa-pied-piper-alt', 'fa fa-pinterest',
			'fa fa-pinterest-p', 'fa fa-pinterest-square', 'fa fa-qq',
			'fa fa-ra', 'fa fa-rebel', 'fa fa-reddit', 'fa fa-reddit-square',
			'fa fa-renren', 'fa fa-safari', 'fa fa-sellsy', 'fa fa-share-alt',
			'fa fa-share-alt-square', 'fa fa-shirtsinbulk',
			'fa fa-simplybuilt', 'fa fa-skyatlas', 'fa fa-skype',
			'fa fa-slack', 'fa fa-slideshare', 'fa fa-soundcloud',
			'fa fa-spotify', 'fa fa-stack-exchange', 'fa fa-stack-overflow',
			'fa fa-steam', 'fa fa-steam-square', 'fa fa-stumbleupon',
			'fa fa-stumbleupon-circle', 'fa fa-tencent-weibo', 'fa fa-trello',
			'fa fa-tripadvisor', 'fa fa-tumblr', 'fa fa-tumblr-square',
			'fa fa-twitch', 'fa fa-twitter', 'fa fa-twitter-square',
			'fa fa-viacoin', 'fa fa-vimeo', 'fa fa-vimeo-square', 'fa fa-vine',
			'fa fa-vk', 'fa fa-wechat', 'fa fa-weibo', 'fa fa-weixin',
			'fa fa-whatsapp', 'fa fa-wikipedia-w', 'fa fa-windows',
			'fa fa-wordpress', 'fa fa-xing', 'fa fa-xing-square',
			'fa fa-y-combinator', 'fa fa-y-combinator-square', 'fa fa-yahoo',
			'fa fa-yc', 'fa fa-yc-square', 'fa fa-yelp', 'fa fa-youtube',
			'fa fa-youtube-play', 'fa fa-youtube-square', 'fa fa-ambulance',
			'fa fa-h-square', 'fa fa-heart', 'fa fa-heart-o',
			'fa fa-heartbeat', 'fa fa-hospital-o', 'fa fa-medkit',
			'fa fa-plus-square', 'fa fa-stethoscope', 'fa fa-user-md',
			'fa fa-wheelchair', 'fa fa-angle-down', 'fa fa-check',
			'fa fa-share', 'fa fa-bar-chart-o', 'fa fa-user',
			'fa fa-shopping-cart', 'fa fa-user', 'fa fa-bell-o',
			'fa fa-briefcase', 'fa fa-check', 'fa fa-share',
			'fa fa-bar-chart-o', 'fa fa-user', 'fa fa-shopping-cart',
			'fa fa-user', 'fa fa-bell-o', 'fa fa-briefcase' ]; 
</script>