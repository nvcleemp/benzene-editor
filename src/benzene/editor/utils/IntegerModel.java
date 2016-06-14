package benzene.editor.utils;

/**
 *
 * @author nvcleemp
 */
public class IntegerModel extends Model{
    
    private int value;

    public IntegerModel() {
        this(0);
    }

    public IntegerModel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if(this.value != value){
            this.value = value;
            invalidate();
        }
    }
    
    public void increase(){
        this.value++;
        invalidate();
    }
    
    public void decrease(){
        this.value--;
        invalidate();
    }
}
