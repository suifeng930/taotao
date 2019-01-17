<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">  </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
    //在contentCategory 所在标签创建一棵树
	$("#contentCategory").tree({
		url : '/content/category/list',
		animate: true,
		method : "GET",
        //右击鼠标
		onContextMenu: function(e,node){
            //关闭原来的鼠标的默认事件
		    e.preventDefault();
		    //选中右击鼠标的节点
            $(this).tree('select',node.target);
            //展示带菜单栏
            $('#contentCategoryMenu').menu('show',{
                //在鼠标的位置显示
                left: e.pageX,
                top: e.pageY
            });
        },
        //在添加的节点被编辑之后触发
        onAfterEdit : function(node){
		    //获取树本身
        	var _tree = $(this);
        	//表示  新增的节点
        	if(node.id == 0){
        		// 新增节点
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        			    //更新节点
        				_tree.tree("update",{
            				target : node.target,//跟新的节点
            				id : data.data.id//跟新 新增节点的id+
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
//处理点击菜单的事件
function menuHandler(item){ //获取
    //获取树节点
	var tree = $("#contentCategory");
	//获取被选中的节点
	var node = tree.tree("getSelected");
	//判断选中的是 添加 删除 重命名
    //点击添加按钮
	if(item.name === "add"){
	    //在被点击的节点上 追加子节点
		tree.tree('append', {
            parent: (node?node.target:null),//判断被追加的子节点的父节点
            data: [{
                text: '新建分类',//节点内容
                id : 0,//节点id
                parentId : node.id //添加节点的父节点id
            }]
        });
		//找到id为零的节点  要添加的节点
		var _node = tree.tree('find',0);//根节点
        //选中id为零的节点  开启编辑模式
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>