package rodney.portaparty;

/**
 * Created by Scott on 4/8/2016.
 */
public class Item {
    private String item;
    private String member;

    public Item(){ }

    public Item(String item, String member) {
        this.item = item;
        this.member = member;
    }
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}