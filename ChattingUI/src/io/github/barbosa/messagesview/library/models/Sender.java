package io.github.barbosa.messagesview.library.models;

public class Sender {

    private String firstName;
    private String lastName;
    private String imageURL;
    private boolean isMe;

    public Sender(String firstName, String lastName, String imageURL) {
        this(firstName, lastName, imageURL, false);
    }

    public Sender(String firstName, String lastName, String imageURL, boolean isMe) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.isMe = isMe;
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

    public String getFirstAndLastName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }

        if (firstName != null) {
            return firstName;
        }

        if (lastName != null) {
            return lastName;
        }

        return "";
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }
}
