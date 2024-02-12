public class users {

    String id;
    String usertype,date,time;
    String status = "Active";

    public void setId(String id){
        this.id = id;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }



    public users(String id, String usertype, String date, String time, String status) {
        this.id = id;
        this.usertype = usertype;
        this.date = date;
        this.time = time;
        this.status = status;

    }

    public static void printUser(users user)
    {
        System.out.println();
        System.out.println("User ID: "+ user.id);
        System.out.println("Usertype: "+ user.usertype);
        System.out.println("Date: "+ user.date);
        System.out.println("Time: "+ user.time);
        System.out.println("Status: " + user.status);
        System.out.println();

    }



}
