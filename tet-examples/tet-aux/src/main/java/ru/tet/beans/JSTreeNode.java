package ru.tet.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JSTreeNode {


	
	
	String id;
	String text;

	//дочерние узлы при получении данных через ajax не нужны
	@JsonIgnore
	List<JSTreeNode> children = new ArrayList<>(0);
	
	TestAuxInfo auxInfo;
	
	//признак, который будет указывать наличие дочерних узлов
	@JsonProperty(value = "children")
	public boolean hasChilds() {
		return children.size()>0;
	}
	
	
	public JSTreeNode(String id, String text) {
		this.id = id;
		this.text = text;
	}
	public JSTreeNode() {
	}
	
	


	public JSTreeNode getChild(int... inds) {
		
		JSTreeNode current = this;
		for (int i = 0; i < inds.length; i++) {
			int ind = inds[i];
			current = current.getChild(ind);
		}
		return current;
	}
	
	public JSTreeNode getChild(int ind) {
		return children.get(ind);
	}
	
	
}
