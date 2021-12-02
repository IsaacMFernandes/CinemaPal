package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data;

public class DiscoverFilm
{
    private String imageURL;

    public DiscoverFilm(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
