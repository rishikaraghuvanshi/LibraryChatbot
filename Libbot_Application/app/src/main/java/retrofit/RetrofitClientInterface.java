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

/**
 * Created by SONY on 2/17/2018.
 */

public interface RetrofitClientInterface {

    @GET("/user/get/{user_id}")
    Call<UserBean> getUserByUsername(@Path("user_id") String user_id);

    @PUT("/user/add")
    Call<ResponseBean> addUser(@Body UserBean userBean);

    @GET("/books/all")
    Call<ArrayList<BookBean>> getAllBooks();
}
