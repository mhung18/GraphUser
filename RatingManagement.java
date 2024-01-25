import java.util.*;
import java.io.*;

public class RatingManagement {
    private ArrayList<Rating> ratings;
    private ArrayList<Movie> movies;
    private ArrayList<User> users;

    // @Requirement 1
    public RatingManagement(String moviePath, String ratingPath, String userPath) {
        this.movies = loadMovies(moviePath);
        this.users = loadUsers(userPath);
        this.ratings = loadEdgeList(ratingPath);
    }

    private ArrayList<Rating> loadEdgeList(String ratingPath) {
        try{
            ratings = new ArrayList<Rating>();
            FileReader fr = new FileReader(ratingPath);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String info = sc.nextLine();
                if(info.equals("UserID,MovieID,Rating,Timestamp")){
                    continue;
                }
                String data[] = info.split(",");
                ratings.add(new Rating(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Long.parseLong(data[3])));
            }
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        

        return ratings;
    }

    private ArrayList<Movie> loadMovies(String moviePath) {
        try{
            movies = new ArrayList<Movie>();
            FileReader fr = new FileReader(moviePath);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String info = sc.nextLine();
                if(info.equals("MovieID,Title,Genres")){
                    continue;
                }
                String data[] = info.split(",");
                ArrayList<String> genres = new ArrayList<String>();
                String kindOfFilm[] = data[2].split("-");
                for (String i : kindOfFilm){
                    genres.add(i);
                }
                movies.add(new Movie(Integer.parseInt(data[0]),data[1],genres));
            }
            fr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return movies; 
    }

    private ArrayList<User> loadUsers(String userPath) {
        try {
            users = new ArrayList<User>();
            FileReader fr = new FileReader(userPath);
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String info = sc.nextLine();
                if(info.equals("UserID,Gender,Age,Occupation,Zip-code")){
                    continue;
                }
                String data[] = info.split(",");
                users.add(new User(Integer.parseInt(data[0]),data[1],Integer.parseInt(data[2]),data[3],data[4]));
            }
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Rating> getRating() {
        return ratings;
    }

    // @Requirement 2
    public ArrayList<Movie> findMoviesByNameAndMatchRating(int userId, int rating) {
        ArrayList<Movie> findMovies = new ArrayList<Movie>();
        for (Rating i : this.getRating()){
            if (i.getUserId() == userId && i.getRating() >= rating){
                for (Movie j : getMovies()){
                    if(j.getId() == i.getMovieId()){
                        findMovies.add(j);
                    }
                }
            }
        }

        for (int i = 0;i < findMovies.size();i++){
            for (int j = 0;j < findMovies.size();j++){
                if(findMovies.get(i).getName().compareTo(findMovies.get(j).getName()) < 0){
                    Movie temp = findMovies.get(i);
                    findMovies.set(i,findMovies.get(j));
                    findMovies.set(j,temp);
                }
            }

        }
        return findMovies; /* change here */
    }

    // Requirement 3
    public ArrayList<User> findUsersHavingSameRatingWithUser(int userId, int movieId) {
        ArrayList<User> findUsers = new ArrayList<User>();
        ArrayList<Integer> usersId = new ArrayList<Integer>();
        int userRate = 0;
        for(Rating i : this.getRating()){
            if (i.getUserId() == userId && i.getMovieId() == movieId){
                userRate = i.getRating();
            }
        }
        for (Rating j : this.getRating()){
            if (j.getRating() >= userRate && j.getMovieId() == movieId && j.getUserId() != userId){
                usersId.add(j.getUserId());
            }
        }
        for (int i : usersId){
            for (User user : this.getUsers()){
                if (user.getId() == i){
                    findUsers.add(user);
                    continue;
                }
            }
        }
        
        return findUsers;
    }
    // Requirement 4
    public ArrayList<String> findMoviesNameHavingSameReputation() {
        ArrayList<Integer> listIdOfFavouriteMovie = new ArrayList<Integer>();
        HashSet<Integer> listIdOfFavouriteMovieByPeople = new HashSet<Integer>();
        ArrayList<Integer> listIdOfFavouriteMovieByOnePerson = new ArrayList<Integer>();
        ArrayList<String> moviesNameHavingSameReputation = new ArrayList<String>();
        for (Rating i : this.getRating()){
            if (i.getRating() > 3){
                listIdOfFavouriteMovie.add(i.getMovieId());
            }
        }
        for (Integer i : listIdOfFavouriteMovie){
            if(listIdOfFavouriteMovieByOnePerson.contains(i)){
                listIdOfFavouriteMovieByPeople.add(i);
            }
            listIdOfFavouriteMovieByOnePerson.add(i);
        }

        HashSet<String> movies = new HashSet<String>();
        for (Integer i : listIdOfFavouriteMovieByPeople){
            for (Movie m : this.getMovies()){
                if (m.getId() == i){
                    movies.add(m.getName());
                }
            }
        } 
        for (String movieName : movies){
            moviesNameHavingSameReputation.add(movieName);
        }
        Collections.sort(moviesNameHavingSameReputation);

        return moviesNameHavingSameReputation;
    }

    // @Requirement 5
    public ArrayList<String> findMoviesMatchOccupationAndGender(String occupation, String gender, int k,int rating) {
        ArrayList<String> moviesMatchOccupationAndGender = new ArrayList<String>();
        ArrayList<Integer> usersHadSameOccupationAndGender = new ArrayList<Integer>();
        for (User u : this.getUsers()){
            if(u.getOccupation().equals(occupation) && u.getGender().equals(gender)){
                usersHadSameOccupationAndGender.add(u.getId());
            }
        }
        ArrayList<Integer> idMoviesOfUserHadSameOccupationAndGender = new ArrayList<Integer>();
        for (Rating rate : this.getRating()){
            if (usersHadSameOccupationAndGender.contains(rate.getUserId()) && rate.getRating() == rating && !idMoviesOfUserHadSameOccupationAndGender.contains(rate.getMovieId())){
                idMoviesOfUserHadSameOccupationAndGender.add(rate.getMovieId());
            }
        }
        
        for (Integer id : idMoviesOfUserHadSameOccupationAndGender){
            for (Movie m : this.getMovies()){
                if(m.getId() == id){
                    moviesMatchOccupationAndGender.add(m.getName());
                }
            }
        }
        Collections.sort(moviesMatchOccupationAndGender);
        ArrayList<String> result = new ArrayList<String>();

        for (String s : moviesMatchOccupationAndGender){
            if(result.size() == k){
                break;
            }
            result.add(s);
        } 
        return result; /* change here */
    }

    // @Requirement 6
    public ArrayList<String> findMoviesByOccupationAndLessThanRating(String occupation, int k, int rating) {
        ArrayList<String> moviesByOccupationAndLessThanRating = new ArrayList<String>();
        ArrayList<Integer> usersHadSameOccupation = new ArrayList<Integer>();
        for (User u : this.getUsers()){
            if(u.getOccupation().equals(occupation)){
                usersHadSameOccupation.add(u.getId());
            }
        }
        ArrayList<Integer> idMoviesOfUserHadSameOccupation = new ArrayList<Integer>();
        for (Rating rate : this.getRating()){
            if(usersHadSameOccupation.contains(rate.getUserId()) && rate.getRating() < rating){
                idMoviesOfUserHadSameOccupation.add(rate.getMovieId());
            }
        }
        for (Integer i : idMoviesOfUserHadSameOccupation){
            for (Movie m : this.getMovies()){
                if(m.getId() == i && !moviesByOccupationAndLessThanRating.contains(m.getName())){
                    moviesByOccupationAndLessThanRating.add(m.getName());
                }
            }
        }
        Collections.sort(moviesByOccupationAndLessThanRating);
        ArrayList<String> result = new ArrayList<String>();
        for (String s : moviesByOccupationAndLessThanRating){
            if(result.size() == k){
                break;
            }
            result.add(s);
        }
        return result; /* change here */
    }

    // @Requirement 7
    public ArrayList<String> findMoviesMatchLatestMovieOf(int userId, int rating, int k) {
        ArrayList<String> findMovies = new ArrayList<String>();
        ArrayList<Integer> userIDhasGenderSameUser = new ArrayList<Integer>();
        for (User user : this.getUsers()){
            if(user.getId() == userId){
                for (User u : this.getUsers()){
                    if (u.getGender() == user.getGender()){
                        userIDhasGenderSameUser.add(u.getId());
                    }
                }
            }
            break;
        }
        ArrayList<Rating> rateByUser = new ArrayList<Rating>();
        long tmp = 0;
        for (Rating rate : this.getRating()){
            if (rate.getUserId() == userId && rate.getTimestamps() > tmp){
                rateByUser.clear();
                rateByUser.add(rate);
                tmp = rate.getTimestamps();
            }
        }
        ArrayList<Integer> rateByUserSameGenderAndHigherThanK = new ArrayList<Integer>();
        for (Rating rate : this.getRating()){
            if (userIDhasGenderSameUser.contains(rate.getUserId()) && rate.getRating() >= rating){
                rateByUserSameGenderAndHigherThanK.add(rate.getMovieId());
            }
        }

        ArrayList<String> nameMovies = new ArrayList<String>();
        for (Rating i : rateByUser){
            for (Movie m : this.getMovies()){
                if (m.getId() == i.getMovieId()){
                    for (Movie movie : this.getMovies()){
                        if(!rateByUserSameGenderAndHigherThanK.contains(movie.getId())){
                            nameMovies.add("Error");
                        }
                        for (String s : movie.getGenres()){
                            if (m.getGenres().contains(s) && !nameMovies.contains(movie.getName())){
                                nameMovies.add(movie.getName());
                                continue;
                            }
                        }
                    } 
                }
                break;
            }
        }
        Collections.sort(nameMovies);
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0;i < k;i++){
            for (String s : nameMovies){
                result.add(s);
            }
        }
        return nameMovies;
    }             
}