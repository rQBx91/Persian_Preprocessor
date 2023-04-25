import java.util.*;

public class PostingList {

    private List<Integer> docIds = new ArrayList<>();

    public PostingList() {

    }

    public PostingList(int... ids) {
        for (int id:ids) {
            docIds.add(id);
        }
        sort();
    }

    public void add(int id){
        docIds.add(id);
    }

    public void sort(){
        Collections.sort(docIds);
    }

    public int size(){
        return docIds.size();
    }

    @Override
    public String toString() {
        return Arrays.toString(docIds.toArray());
    }

    public List<Integer> getDocIds() {
        return docIds;
    }

    public PostingList and(PostingList other){
        PostingList result = new PostingList();
        int i = 0, j = 0;
        while (i < this.size() && j < other.size() ){
            int a = docIds.get(i);
            int b = other.docIds.get(j);
            if(a==b){
                result.add(a);
                i++;
                j++;
            } else if (a < b){
                i++;
            } else {
                j++;
            }
        }
        return result;
    }

    public PostingList or(PostingList other){

        Set<Integer> union = new HashSet<>();
        union.addAll(this.docIds);
        union.addAll(other.docIds);

        PostingList result = new PostingList();
        for (Integer s:union){
            result.add(s);
        }
        result.sort();
        return result;
    }

    public PostingList not(DocStore docStore) {
        Integer[] allKeys = docStore.getAllIds();
        PostingList result = new PostingList();
        for (Integer key : allKeys) {
            if (!this.docIds.contains(key)) {
                result.add(key);
            }

        }
        return result;
    }



}
