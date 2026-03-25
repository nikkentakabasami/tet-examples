package ru.tet.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ru.tet.beans.JSTreeNode;
import ru.tet.beans.TestAuxInfo;

public class JSTreeDataSamples {

	private static final JSTreeDataSamples instance = new JSTreeDataSamples();
	public static JSTreeDataSamples getInstance() {
		return instance;
	}
	
	int newNodeCounter = 100;
	int counter = 0; 
	
	
	JSTreeNode tree1;
	Map<String,JSTreeNode> nodesMap = new HashMap<>();
	

	private JSTreeDataSamples() {
		init();
	}

	
	public void init() {
		if (tree1!=null) {
			return;
		}
		
		tree1 = new JSTreeNode(genId(),"Корень");
		nodesMap.put(tree1.getId(), tree1);
		addChilds(tree1,"a","b","c","d","e");
		addChilds(tree1.getChild(0),"A1","A2","A3","A4");
		addChilds(tree1.getChild(1),"B1","B2","B3","B4");
		addChilds(tree1.getChild(2),"C1","C2","C3","C4");
		addChilds(tree1.getChild(0,1),"X1","X2","X3");
		
		tree1.getChild(1).setAuxInfo(new TestAuxInfo(counter++, "table", "tsukue"));
		tree1.getChild(2).setAuxInfo(new TestAuxInfo(counter++, "bag", "kaban"));
		
	}
	
	public JSTreeNode findNode(String id) {
		return nodesMap.get(id);
	}
	
	
	public void addNode(String parentId) {
		JSTreeNode parent = findNode(parentId);
		newNodeCounter++;
		addChilds(parent, "newNode"+newNodeCounter);
	}
	
	
	public JSTreeNode getTree1() {
		return tree1;
	}
	
	
	String genId() {
		counter++;
		return "tn"+counter;
	}
	
	
	public List<JSTreeNode> findChildren(String parentId) {
		if (StringUtils.isEmpty(parentId) || "#".equals(parentId)) {
//			JSTreeNode root = nodesMap.get("tn1");
			return Arrays.asList(tree1);
		}
		
		JSTreeNode node = nodesMap.get(parentId);
		if (node!=null) {
			return node.getChildren();
		}
		return new ArrayList<>(0);
	}	
	
	public void addChilds(JSTreeNode parentNode, String... texts) {
		for (int i = 0; i < texts.length; i++) {
			
			
			JSTreeNode node = new JSTreeNode(genId(),texts[i]);
			nodesMap.put(node.getId(), node);
			parentNode.getChildren().add(node);
			
		}
	}	
	
	
	
	
	
}
