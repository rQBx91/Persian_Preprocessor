import java.util.concurrent.atomic.AtomicInteger;

public class Doc {

    private static final AtomicInteger lastId = new AtomicInteger(0);
    private final int docId;
    private final String docName;
    private final String docBody;


    public Doc(String docName, String docBody) {
        this.docId = lastId.incrementAndGet();
        this.docName = docName;
        this.docBody = docBody;
    }

    public int getDocId() {
        return docId;
    }

    public AtomicInteger getLastId() {
        return lastId;
    }

    public String getDocName() {
        return docName;
    }

    public String getDocBody() {
        return docBody;
    }

    @Override
    public String toString() {
        return "Document{" +
                "Id=" + docId +
                ", Name='" + docName + "\',\n" +
                "Body='" + docBody + "\'\n" +
                "}";
    }
}
