//package de.lojaw.tttmod;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import com.microsoft.aad.msal4jextensions.*;
//
//import com.microsoft.aad.msal4j.*;
//import com.microsoft.aad.msal4jextensions.PersistenceTokenCacheAccessAspect;
//
//import com.microsoft.aad.msal4j.*;
//import com.microsoft.aad.msal4jextensions.PersistenceSettings;
//import com.microsoft.aad.msal4jextensions.PersistenceTokenCacheAccessAspect;
//
//public class MicrosoftLoginHandler {
//    private static final String CLIENT_ID = "*****************";
//    private static final String AUTHORITY = "https://login.microsoftonline.com/common/";
//    private static final String[] SCOPES = {"User.Read", "XboxLive.signin"};
//
//    public static void login() {
//        try {
//            LoggerUtils.logger.info("com.microsoft.aad.msal4j").setLevel(Level.FINEST);
//
//
//            ITokenCacheAccessAspect persistenceAspect = new PersistenceTokenCacheAccessAspect(createPersistenceSettings());
//            PublicClientApplication app = PublicClientApplication.builder(CLIENT_ID)
//                    .authority(AUTHORITY)
//                    .setTokenCacheAccessAspect(persistenceAspect)
//                    .build();
//
//            Set<String> scopes = new HashSet<>(Arrays.asList(SCOPES));
//
//            String username = System.getProperty("user.name");
//            LoggerUtils.logger.info("Username: " + username);
//
//            IntegratedWindowsAuthenticationParameters parameters =
//                    IntegratedWindowsAuthenticationParameters.builder(scopes, username)
//                            .build();
//
//            CompletableFuture<IAuthenticationResult> future = app.acquireToken(parameters);
//
//            future.handle((result, ex) -> {
//                if (result != null) {
//                    String accessToken = result.accessToken();
//                    fetchMinecraftPlayerName(accessToken);
//                } else {
//                    ex.printStackTrace();
//                }
//                return null;
//            });
//        } catch (Exception e) {
//            LoggerUtils.logger.info("Error during login: " + e);
//        }
//    }
//
//    private static PersistenceSettings createPersistenceSettings() {
//        // Beispiel für Windows:
//        String cacheDirectory = "C:/Users/jpsch/AppData/Roaming/.minecraft/mods";
//        Path cacheDirPath = Paths.get(cacheDirectory);
//
//        return PersistenceSettings.builder("cacheName", cacheDirPath)
//                .build();
//    }
//
//    private static void fetchMinecraftPlayerName(String accessToken) {
//        try {
//            // Erstelle einen HttpClient
//            org.apache.http.client.HttpClient httpClient = org.apache.http.impl.client.HttpClientBuilder.create().build();
//
//            // Erstelle eine GET-Anfrage an die Minecraft Services-API
//            org.apache.http.client.methods.HttpGet request = new org.apache.http.client.methods.HttpGet("https://api.minecraftservices.com/minecraft/profile");
//            request.setHeader("Authorization", "Bearer " + accessToken);
//
//            // Führe die Anfrage aus
//            org.apache.http.HttpResponse response = httpClient.execute(request);
//
//            // Überprüfe den Antwort-Statuscode
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == org.apache.http.HttpStatus.SC_OK) {
//                // Lese den Antwort-Inhalt
//                String responseBody = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
//
//                // Parse den JSON-Inhalt
//                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonParser().parse(responseBody).getAsJsonObject();
//
//                // Extrahiere den Spielernamen aus der Antwort
//                String playerName = jsonObject.get("name").getAsString();
//
//                // Setze den Spielernamen
//                setPlayerName(playerName);
//            } else {
//                // Behandle den Fehlerfall
//                System.out.println("Fehler beim Abrufen des Minecraft-Spielernamens. Statuscode: " + statusCode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void setPlayerName(String playerName) {
//        com.mojang.authlib.GameProfile gameProfile = new com.mojang.authlib.GameProfile(null, playerName);
//
//        net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
//        net.minecraft.client.User user = minecraft.getUser();
//        if (user != null) {
//            try {
//                // Aktualisiere das GameProfile des Benutzers
//                java.lang.reflect.Field gameProfileField = user.getClass().getDeclaredField("gameProfile");
//                gameProfileField.setAccessible(true);
//                gameProfileField.set(user, gameProfile);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Fehler beim Setzen des Spielernamens: User ist null");
//        }
//    }
//}