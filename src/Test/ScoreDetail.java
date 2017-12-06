import java.io.Serializable;

/**
 * Created by UC227911 on 12/6/2017.
 */
public class ScoreDetail implements Serializable{

    public String studentName;
    public String subject;
    public float score;

    public ScoreDetail(String studentName, String subject, float score) {
        this.studentName = studentName;
        this.subject = subject;
        this.score = score;
    }


}
