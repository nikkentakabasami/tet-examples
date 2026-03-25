package ru.tet.beans;

import lombok.Data;

@Data
public class SuIdNameModel {

  Integer id;

  String name;

  public SuIdNameModel(Integer id, String name) {
    this.id = id;
    this.name = name;
  }


}
