
import java.util.Collection;
import java.util.HashMap;

public class DocStore {

    private HashMap<Integer, Doc> allDocs = new HashMap<>();

    public void add(Doc doc){
        allDocs.put(doc.getDocId(), doc);
    }

    public Doc get(int id){
        return allDocs.get(id);
    }

    public Collection<Doc> getAll(){
        return allDocs.values();
    }

    public Integer[] getAllIds(){
        Integer[] all = new Integer[allDocs.size()];
        int i = 0;
        for (Integer key:allDocs.keySet()){
            all[i] = key;
            i++;
        }
        return all;
    }

}
