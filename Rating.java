public class Rating {
    private int attr1 ;
    private int attr2 ;
    private int attr3 ;
    private long attr4 ;

    public Rating(int userId, int movieId, int rating,long timestamps){
        this.attr1 = userId;
        this.attr2 = movieId;
        this.attr3 = rating;
        this.attr4 = timestamps;
    }
    public int getUserId(){
        return attr1;
    }
    public void setUserId(int id){
        this.attr1 = id;
    }
    public int getMovieId(){
        return attr2;
    }
    public void setMovieId(int movieid){
        this.attr2 = movieid;
    }
    public int getRating(){
        return attr3;
    }
    public void setRating(int rate){
        this.attr3 = rate;
    }
    public long getTimestamps(){
        return attr4;
    }
    public void setTimestamps(long time){
        this.attr4 = time;
    }

    @Override
    public String toString(){
        return String.format("Rating[%d, %d, %d, %ld]",attr1,attr2,attr3,attr4);                            
    }
}