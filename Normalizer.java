import utility.MakeTrans;
import utility.RegexPattern;
import java.util.ArrayList;
import java.util.List;


public class Normalizer {
    public static Normalizer instance;
    private final String puncAfter = "!:\\.،؛؟»\\]\\)\\}";
    private final String puncBefore = "«\\[\\(\\{";
    private boolean characterRefinement = true;
    private List<RegexPattern> characterRefinementPatterns;
    private boolean punctuationSpacing = true;
    private List<RegexPattern> punctuationSpacingPatterns;
    private boolean affixSpacing = true;
    private List<RegexPattern> affixSpacingPatterns;
    private MakeTrans translations;

    public Normalizer() {
        this(true, true, true);
    }

    public Normalizer(boolean characterRefinement, boolean punctuationSpacing, boolean affixSpacing) {
        this.characterRefinement = characterRefinement;
        this.punctuationSpacing = punctuationSpacing;
        this.affixSpacing = affixSpacing;

        this.translations = new MakeTrans("ؤ ئ كي;%1234567890", "و ی کی؛٪۱۲۳۴۵۶۷۸۹۰");


        if (this.characterRefinement) {
            this.characterRefinementPatterns = new ArrayList<>();

            this.characterRefinementPatterns.add(new RegexPattern("[ـ\\r]", ""));

            this.characterRefinementPatterns.add(new RegexPattern(" +", " "));

            this.characterRefinementPatterns.add(new RegexPattern("\n\n+", "\n\n"));

            this.characterRefinementPatterns.add(new RegexPattern(" ?\\.\\.\\.", " …"));
        }

        if (this.punctuationSpacing) {
            this.punctuationSpacingPatterns = new ArrayList<>();

            this.punctuationSpacingPatterns.add(new RegexPattern(" ([" + puncAfter + "])", "$1"));

            this.punctuationSpacingPatterns.add(new RegexPattern("([" + puncBefore + "]) ", "$1"));

            this.punctuationSpacingPatterns.add(new RegexPattern("([" + puncAfter + "])([^ " + puncAfter + "])", "$1 $2"));

            this.punctuationSpacingPatterns.add(new RegexPattern("([^ " + puncBefore + "])([" + puncBefore + "])", "$1 $2"));
        }

        if (this.affixSpacing) {
            this.affixSpacingPatterns = new ArrayList<>();

            this.affixSpacingPatterns.add(new RegexPattern("([^ ]ه) ی ", "$1‌ی "));

            this.affixSpacingPatterns.add(new RegexPattern("(^| )(ن?می) ", "$1$2‌"));

            this.affixSpacingPatterns.add(new RegexPattern(" (تر(ی(ن)?)?|ها(ی)?)(?=[ \n" + puncAfter + puncBefore + "]|$)", "‌$1"));

            this.affixSpacingPatterns.add(new RegexPattern("([^ ]ه) (ا(م|ت|ش|ی))(?=[ \n" + puncAfter + "]|$)", "$1‌$2"));
        }
    }

    public static Normalizer i() {
        if (instance != null) return instance;
        instance = new Normalizer();
        return instance;
    }

    public String run(String text) {
        if (this.characterRefinement)
            text = characterRefinement(text);

        if (this.punctuationSpacing)
            text = punctuationSpacing(text);

        if (this.affixSpacing)
            text = affixSpacing(text);

        return text;
    }

    private String characterRefinement(String text) {
        text = this.translations.translate(text);
        for (RegexPattern pattern : this.characterRefinementPatterns)
            text = pattern.apply(text);
        return text;
    }

    private String punctuationSpacing(String text) {
        for (RegexPattern pattern : this.punctuationSpacingPatterns)
            text = pattern.apply(text);
        return text;
    }

    private String affixSpacing(String text) {
        for (RegexPattern pattern : this.affixSpacingPatterns)
            text = pattern.apply(text);
        return text;
    }
}
