package youtube;

public class Review {
    protected String url;
    protected String username;
    protected String remark;
    protected char score;
    protected int reviewId;
 
    public Review() {
    }
    
    public Review(String url, String username, String remark, char score)
    {
        this.url = url;
        this.username = username;
        this.remark = remark;
        this.score = score;
        //this.reviewId = reviewId;
    }
 
    public void setUrl(String url)
    {
    	this.url = url;
    }
 
    public String getUrl() {
        return url;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername()
    {
    	return username;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getRemark()
    {
    	return remark;
    }
    
    public void setScore(char score)
    {
    	this.score = score;
    }
    
    public char getScore()
    {
    	return score;
    }
    
    public void setReviewid(int reviewId)
    {
    	this.reviewId = reviewId;
    }
    
    public int getReviewid()
    {
    	return reviewId;
    }
}