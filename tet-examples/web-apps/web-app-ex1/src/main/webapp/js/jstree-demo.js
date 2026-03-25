

let $jstree1;
let $jstree2;



$(() => {

	//смена общего стиля
	//	$.jstree.defaults.core.themes.variant = "large";	
	
	$jstree1 = $('#jstree1');
	$jstree2 = $('#jstree2');
	
	

	
	
	//данные для дерева будут передаваться через ajax в json-формате
	//JSTreeServlet
	$jstree1.jstree({
	  'core': {
			'data' : {
			  'url' : 'jsTree/getNode2',
			  'data' : function (node) {
			    return { 'id' : node.id };
			  }
			}
	  }
	});
	
	
	
	
	//данные для дерева будут передаваться через ajax в html-формате
	//jstreeDemo1Nodes.jsp

	$jstree2.jstree({
	  'core': {
			'data' : {
			  'url' : 'aux_jsp/jstreeDemo1Nodes.jsp',
			  'data' : function (node) {
			    return { 'id' : node.id };
			  }
			}
	  }
	});			
	


	$('#btn4').click(()=>{
		
		let tree = $jstree1.jstree(true);
		
		let selectedIds = tree.get_selected();
		if (selectedIds.length==0){
			return;
		}
		
		let parentId = selectedIds[0];
		
		$.post({
			url : 'jsTree/addNode1',
			data: { 'parentId': parentId },
		  success: function(data, status, request){
				tree.refresh_node(parentId);
				tree.open_node(parentId);
		  },
		});		

	});	
	

});
