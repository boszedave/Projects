package advisor;

import com.neovisionaries.i18n.CountryCode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfCategoriesRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfNewReleasesRequest;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        String C_NAME = "";
        String input;
        final String CLIENT_ID = "CLIENTID";
        final String CLIENT_SECRET = "CLIENTSECRET";
        final String code = "";
        final String[] token = {""};
        boolean auth = false;
        HttpServer server = null;

        System.out.println("Please, give access to the application by using \"auth\" keyword. Type \"exit\" to stop the application.");

        do {
            input = scan.nextLine();
            if (!input.equals("auth") && !auth) {
                System.out.println("Please, provide access for application.");
            }
            if (input.equals("auth")) {
                System.out.println("Use these keywords:");
                System.out.println("\"new\" - list of new albums with artists and links");
                System.out.println("\"featured\" - list of Spotify featured playlists with their links");
                System.out.println("\"categories\" - list of all available categories");
                System.out.println("\"playlists C_NAME\" -  where C_NAME is the name of category (i.e. sleep). List contains playlists of this category and their links");

                auth = true;
                final String clientId = "b6c6c841bbb6417f9715315c2838f140";
                String clientSecret = "d7f231d4a586495dbe6fe405ca6c74f7";
                URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback");
                server = HttpServer.create();
                server.bind(new InetSocketAddress(8080), 0);
                server.start();

                System.out.println("Use this link to request the access code:");
                System.out.println("https://accounts.spotify.com/authorize?client_id=" +
                        CLIENT_ID + "&redirect_uri=" + redirectUri + "&response_type=code");

                createServerContext(token, server, clientId, clientSecret, redirectUri);
            }

            if (input.contains("playlists")) {
                C_NAME = input.substring(10);
                input = "playlists";
            }
            if (auth) {
                switch (input) {
                    case "new":
                        System.out.println("---NEW RELEASES---");
                        getNewReleases(token[0]);
                        break;
                    case "featured":
                        System.out.println("---FEATURED---");
                        getFeaturedPlaylists(token[0]);
                        break;
                    case "categories":
                        System.out.println("---CATEGORIES---");
                        getCategories(token[0]);
                        break;
                    case "playlists":
                        System.out.println("---" + C_NAME + " " + "PLAYLISTS---");
                        getPlaylistsOfCategory(C_NAME, token[0]);
                        break;
                    default:
                        break;
                }
            }
        } while (!input.equals("exit"));
        scan.close();
        server.stop(1);
        System.out.println("---GOODBYE!---");
    }

    private static void createServerContext(String[] token, HttpServer server, String clientId, String clientSecret, URI redirectUri) {
        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String hello = "Test page";
                        exchange.sendResponseHeaders(200, hello.length());
                        exchange.getResponseBody().write(hello.getBytes());
                        String query = exchange.getRequestURI().getQuery();
                        //get the authorization code from callback url
                        query = query.substring(5);
                        exchange.getResponseBody().close();

                        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                                .setClientId(clientId)
                                .setClientSecret(clientSecret)
                                .setRedirectUri(redirectUri)
                                .build();

                        final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(query)
                                .build();

                        try {
                            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
                            // Set access and refresh token for further "spotifyApi" object usage
                            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
                            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
                            token[0] = authorizationCodeCredentials.getAccessToken();
                            System.out.println("Token granted. Expires in: " + authorizationCodeCredentials.getExpiresIn());
                        } catch (IOException | SpotifyWebApiException e) {
                            System.out.println("Access denied");
                        }
                    }
                }
        );
    }

    private static void getNewReleases(String accessToken) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        final GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
                .country(CountryCode.HU)
                .limit(10)
                .build();
        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = getListOfNewReleasesRequest.execute();
            Set<AlbumSimplified> newReleases = new HashSet<>(Set.of(albumSimplifiedPaging.getItems()));
            for (AlbumSimplified item : newReleases) {
                List<ArtistSimplified> artistsAsList = Arrays.asList(item.getArtists());
                for (ArtistSimplified artists : artistsAsList) {
                    System.out.print(" [" + artists.getName()+"]");
                }
                System.out.println(" - " + item.getName());
                System.out.println(item.getHref());
            }
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getFeaturedPlaylists(String accessToken) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        final GetListOfFeaturedPlaylistsRequest getListOfFeaturedPlaylistsRequest = spotifyApi
                .getListOfFeaturedPlaylists()
                .country(CountryCode.HU)
                .limit(20)
                .build();
        try {
            final FeaturedPlaylists featuredPlaylists = getListOfFeaturedPlaylistsRequest.execute();
            System.out.println("Message: " + featuredPlaylists.getMessage() + "\n");
            final Paging<PlaylistSimplified> playlists = featuredPlaylists.getPlaylists();
            List<PlaylistSimplified> playlistAsList = Arrays.asList(playlists.getItems());
            for (PlaylistSimplified name : playlistAsList) {
                System.out.println(name.getName() + '\n' + name.getHref());
            }
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getCategories(String accessToken) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        GetListOfCategoriesRequest getListOfCategoriesRequest = spotifyApi.getListOfCategories()
                .country(CountryCode.HU)
                //.limit(10)
                .build();
        try {
            final Paging<Category> categoryPaging = getListOfCategoriesRequest.execute();
            List<Category> categoriesAsList = Arrays.asList(categoryPaging.getItems());
            for (Category name : categoriesAsList) {
                System.out.println(name.getName());
            }
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getPlaylistsOfCategory(String c_NAME, String accessToken) {
        String categoryId = c_NAME;
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        GetCategorysPlaylistsRequest getCategoryRequest = spotifyApi.getCategorysPlaylists(categoryId)
                .country(CountryCode.HU)
                .limit(10)
                .build();
        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getCategoryRequest.execute();
            List<PlaylistSimplified> playlistsOfCategoriesAsList = Arrays.asList(playlistSimplifiedPaging.getItems());
            for (PlaylistSimplified name : playlistsOfCategoriesAsList) {
                System.out.println(name.getName() + '\n' + name.getHref());
            }
            //System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage()  + " Try to use lowercase or type another category name!");
        }
    }
}
