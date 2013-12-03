package nlp.project;

import java.util.*;
import java.util.Collections.*;
import java.io.*;
import nlp.util.Pair;

public class SentencePair<T>{
  // source and target sentence in T=String or Integer format
  List<T> srcSentence = new ArrayList<T>();
  List<T> trgSentence = new ArrayList<T>();

  // alignment position is counting from 1, where 0 is reserved for NULL
  List<Integer> wa_s2t = new ArrayList<Integer>();
  List<Integer> wa_t2s = new ArrayList<Integer>();

  // read data sequentially from elsewhere
  public void addSrcWord(T i){ srcSentence.add(i); }
  public void addTrgWord(T i){ trgSentence.add(i); }
  public void addWAs2t(int j) { wa_s2t.add(j);}
  public void addWAt2s(int i) { wa_t2s.add(i);}

  public List<T> getSrcSentence(){ return srcSentence; }
  public List<T> getTrgSentence(){ return trgSentence; }
  
  // return an unordered list word alignment tuples
  public List<Pair<Integer, Integer>> getWordAlignmentPairs(){
    List<Pair<Integer, Integer>> ret = new ArrayList<Pair<Integer,Integer>>(); 

    for (int i=0; i<wa_s2t.size(); i++) { 
      int j = wa_s2t.get(i);
      if (j>0) ret.add(Pair.makePair(i+1, j));
    }

    for (int j=0; j<wa_t2s.size(); j++) { 
      int i = wa_t2s.get(j);
      if (i>0) ret.add(Pair.makePair(i, j+1));
    }
    return ret;
  }

  // returns a sorted list of all the target positions that matches srcPos.
  // null is excluded, returned list does not have same elements
  // return empty List if no matches
  public List<Integer> getTrgPositions(int srcPos){
    List<Integer> ret = new ArrayList<Integer>();
    if (srcPos <= 0 || srcPos > srcSentence.size()) return ret;

    for (int j=0; j<wa_t2s.size(); j++) {
      int i = wa_t2s.get(j);
      if (i==srcPos)  { ret.add(j+1);}
    }

    if (srcPos-1 < wa_s2t.size()) {
      int j = wa_s2t.get(srcPos-1);
      if (j>0) {
        int r = Collections.binarySearch(ret, j);  
        if (r<0) {ret.add(-r-1, j);}
      }
    }
    return ret; 
  }

  // same as above, but return all source positions when target position is given
  public List<Integer> getSrcPositions(int trgPos){
    List<Integer> ret = new ArrayList<Integer>();
    if (trgPos <= 0 || trgPos > trgSentence.size()) return ret;

    for (int i=0; i<wa_s2t.size(); i++) {
      int j = wa_s2t.get(trgPos);
      if (j==trgPos)  ret.add(i+1);
    }

    if (trgPos-1 < wa_t2s.size()) {
      int i = wa_t2s.get(trgPos-1);
      if (i>0) {
        int r = Collections.binarySearch(ret, i);  
        if (r<0) ret.add(-r-1, i); 
      }
    }

    return ret; 
  }


  public String toString(){
    String ret = "";
    for (T i : srcSentence)  ret += (" "+i);
    ret += "\n";
    for (T i : trgSentence)  ret += (" "+i);
    ret += "\n";
    return ret;
  }
}

