import java.io.*;
import java.util.*;

public class InvertedIndex {

    private HashMap<String, PostingList> table = new HashMap<>();

    public void add(Doc doc) {

        String message =  doc.getDocBody();
        ArrayList<String> tokens = tokenize(message);

        Set<String> distinctTokens = new HashSet<>(tokens);

        for (String token : distinctTokens) {
            if (token.equals("")) continue;
            table.putIfAbsent(token, new PostingList());
            table.get(token).add(doc.getDocId());
        }

        for (PostingList list : table.values()) {
            list.sort();
        }

    }

    private ArrayList<String> tokenize(String message){
        String regex = "\s\n\r\t\"\\'!~`.?@#$&۰۱۲۴۵۶۷۸۹0123456789,()’_^*:;<>+=-%“—/[]{}|،•٬»«–؛";
        String[] stopwords = {
                "و",
                "در",
                "به",
                "از",
                "كه",
                "مي",
                "اين",
                "است",
                "را",
                "با",
                "هاي",
                "براي",
                "آن",
                "يک",
                "شود",
                "شده",
                "خود",
                "ها",
                "كرد",
                "شد",
                "ای",
                "تا",
                "كند",
                "بر",
                "بود",
                "گفت",
                "نيز",
                "وي",
                "هم",
                "كنند",
                "دارد",
                "ما",
                "كرده",
                "يا",
                "اما",
                "بايد",
                "دو",
                "اند",
                "هر",
                "خواهد",
                "او",
                "مورد",
                "آنها",
                "باشد",
                "ديگر",
                "مردم",
                "نمي",
                "بين",
                "پيش",
                "پس",
                "اگر",
                "همه",
                "صورت",
                "يكي",
                "هستند",
                "بي",
                "من",
                "دهد",
                "هزار",
                "نيست",
                "استفاده",
                "داد",
                "داشته",
                "راه",
                "داشت",
                "چه",
                "همچنين",
                "كردند",
                "داده",
                "بوده",
                "دارند",
                "همين",
                "سوي",
                "شوند",
                "بيشتر",
                "بسيار",
                "روي",
                "گرفته",
                "هايي",
                "تواند",
                "اول",
                "نام",
                "هيچ",
                "چند",
                "جديد",
                "بيش",
                "شدن",
                "كردن",
                "كنيم",
                "نشان",
                "حتي",
                "اينكه",
                "ولی",
                "توسط",
                "چنين",
                "برخي",
                "نه",
                "ديروز",
                "دوم",
                "درباره",
                "بعد",
                "مختلف",
                "گيرد",
                "شما",
                "گفته",
                "آنان",
                "بار",
                "طور",
                "گرفت",
                "دهند",
                "گذاري",
                "بسياري",
                "طي",
                "بودند",
                "ميليارد",
                "بدون",
                "تمام",
                "كل",
                "تر",
                "براساس",
                "شدند",
                "ترين",
                "امروز",
                "باشند",
                "ندارد",
                "چون",
                "قابل",
                "گويد",
                "ديگري",
                "همان",
                "خواهند",
                "قبل",
                "آمده",
                "اكنون",
                "تحت",
                "طريق",
                "گيري",
                "جاي",
                "هنوز",
                "چرا",
                "البته",
                "كنيد",
                "سازي",
                "سوم",
                "كنم",
                "بلكه",
                "زير",
                "توانند",
                "ضمن",
                "فقط",
                "بودن",
                "حق",
                "آيد",
                "وقتي",
                "اش",
                "يابد",
                "نخستين",
                "مقابل",
                "خدمات",
                "امسال",
                "تاكنون",
                "مانند",
                "تازه",
                "آورد",
                "فكر",
                "آنچه",
                "نخست",
                "نشده",
                "شايد",
                "چهار",
                "جريان",
                "پنج",
                "ساخته",
                "زيرا",
                "نزديك",
                "برداري",
                "كسي",
                "ريزي",
                "رفت",
                "گردد",
                "مثل",
                "آمد",
                "ام",
                "بهترين",
                "دانست",
                "كمتر",
                "دادن",
                "تمامي",
                "جلوگيري",
                "بيشتري",
                "ايم",
                "ناشي",
                "چيزي",
                "آنكه",
                "بالا",
                "بنابراين",
                "ايشان",
                "بعضي",
                "دادند",
                "داشتند",
                "برخوردار",
                "نخواهد",
                "هنگام",
                "نبايد",
                "غير",
                "نبود",
                "ديده",
                "وگو",
                "داريم",
                "چگونه",
                "بندي",
                "خواست",
                "فوق",
                "ده",
                "نوعي",
                "هستيم",
                "ديگران",
                "همچنان",
                "سراسر",
                "ندارند",
                "گروهي",
                "سعي",
                "روزهاي",
                "آنجا",
                "يكديگر",
                "كردم",
                "بيست",
                "بروز",
                "سپس",
                "رفته",
                "آورده",
                "نمايد",
                "باشيم",
                "گويند",
                "زياد",
                "خويش",
                "همواره",
                "گذاشته",
                "شش"
        };

        StringTokenizer st = new StringTokenizer(message, regex);
        Normalizer normalizer = new Normalizer();
        ArrayList<String> tokens = new ArrayList<>();

        while(st.hasMoreTokens()){
            String tk = st.nextToken();
            tk = normalizer.run(tk);
            boolean swState = false;
            for (String stopword : stopwords) {
                if (tk.equals(stopword)) {
                    swState = true;
                    break;
                }
            }
            if (swState | tk.length()<=2){
                continue;
            }
            tokens.add(tk);
        }
        return tokens;
    }

    public PostingList get(String token) {
        return table.get(token);
    }

    public boolean saveIndex(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("\nDeleted the file: " + file.getName());
            } else {
                System.out.println("\nFailed to delete the file: " + file.getName());
                return false;
            }
        }

        BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry<String, PostingList> entry : table.entrySet()) {
            buffer.write(entry.getKey() + ":" + entry.getValue());
            buffer.newLine();
        }
        buffer.close();
        return true;
    }

}
