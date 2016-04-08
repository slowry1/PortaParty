package rodney.portaparty;

/**
 * Created by rodney on 4/3/2016.
 */
public class User {

    private String email;
    private String username;
    private String userId;
    private String gender;
    private String firstName;
    private String lastName;

    public User() {
    }

    public User(String email, String username, String userId, String firstName, String lastName, String gender) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}