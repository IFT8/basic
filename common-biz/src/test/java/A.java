import me.ift8.basic.model.Parsable;
import me.ift8.basic.model.Transferable;
import lombok.Data;

/**
 * Created by IFT8 on 2017/5/26.
 */
@Data
public class A implements Parsable<C>, Transferable<B> {
    private String name;

    public static void main(String[] args) {
        A a = new A();
        a.setName("this is a");

        B transform = a.transform();
        System.out.println(transform);
    }
}
