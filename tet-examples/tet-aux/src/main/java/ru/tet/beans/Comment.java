package ru.tet.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {

	Integer id;
	String name;
	String comment;
	
}
