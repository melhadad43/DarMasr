package com.mustafaelhadad.testprojectqr.Model;

import java.util.List;

public class VoteModel {

    private  String id ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String vote;
    private List<String> optionList;


    public VoteModel() {
    }

    public VoteModel(String vote, List<String> optionList,String id) {
        this.vote = vote;
        this.optionList = optionList;
        this.id = id ;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }
    public  void setData (String id){
        this.vote = getVote();
        this.optionList = getOptionList();
        this.id = id ;
    }
}
