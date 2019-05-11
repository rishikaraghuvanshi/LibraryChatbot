package retrofit;

import com.hariofspades.chatbot.Pojo.BookBean;
import com.hariofspades.chatbot.Pojo.ResponseBean;
import com.hariofspades.chatbot.Pojo.UserBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitClientInterface {

    @GET("/user/get/{user_id}")
    Call<UserBean> getUserByUsername(@Path("user_id") String user_id);

    @PUT("/user/add")
    Call<ResponseBean> addUser(@Body UserBean userBean);

    @GET("/books/all")
    Call<ArrayList<BookBean>> getAllBooks();

    @GET("books/search")
    Call<ArrayList<BookBean>> searchBooks(@Query("bookName") String bookName, @Query("author") String author);

    @GET("books/issue")
    Call<ResponseBean> issueBook(@Query("id") int id, @Query("username") String username);

    @GET("books/getCheckout")
    Call<ArrayList<BookBean>> getCheckout(@Query("username") String username);

    @PUT("books/checkout")
    Call<ResponseBean> checkout(@Body ArrayList<BookBean> books, @Query("username") String username);


}
