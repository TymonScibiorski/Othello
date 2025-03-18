package pl.tymsiwojts;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.CorsPluginConfig;
import io.javalin.websocket.WsContext;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    private static final int BOARD_SIZE = 8;
    private static Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Board board = new Board();
    private static final Set<WsContext> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());



    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.http.asyncTimeout = 10_000L;
//                config.plugins.enableCors(cors -> {
//                    cors.add(it -> {
//                        it.allowHost("http://localhost:5173");
//                    });
//                });
            config.bundledPlugins.enableCors(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost));
            config.router.apiBuilder(() -> {
                get("/path", ctx -> ctx.result("Hello, World!"));
                get("/board", ctx -> ctx.json(getBoardState()));
                post("/move", ctx -> {
                    long t1 = System.currentTimeMillis();
                            handleMove(ctx);
                    long t2 = System.currentTimeMillis();
                    log.info("pl.tymiwojts.Move took: {}", t2 - t1);
                        }
                );
                delete("/board", ctx -> {
                    board.initialiseBoard();
                    previousMove = Colour.WHITE.getValue();
                    ctx.status(200);
                });
            });
        }).start(7070);
//        app.ws("/game", ws -> {
//            ws.onConnect(ctx -> {
//                System.out.println("New player connected: " + ctx.sessionId());
//                sessions.add(ctx);
//            });
//
//            ws.onMessage((ctx) -> {
//
//                System.out.println("Received move: " + message);
//                // Broadcast the move to all connected clients
//                for (WsContext session : sessions) {
//                    session.send(message);
//                }
//            });
//
//            ws.onClose(ctx -> {
//                System.out.println("Player disconnected: " + ctx.sessionId());
//                sessions.remove(ctx);
//            });
//        });

    }

    private static int previousMove = Colour.WHITE.getValue();

    private static void handleMove(Context ctx) {
        log.info(ctx.body());
        for (int i = 0; i < BOARD_SIZE; i++) {
            log.info(Arrays.toString(Main.board.getBoardState()[i]));
        }
        Move move = ctx.bodyAsClass(Move.class);
        if(previousMove != move.getColour()) {

            boolean isValidMove = Main.board.performMove(move);

            if (isValidMove) {
                previousMove = move.getColour();
                ctx.status(200);
                ctx.json(getBoardState());
            } else {
                ctx.status(400);
                ctx.json(getBoardState());
            }
        } else {
            ctx.status(400);
            ctx.json(getBoardState());
        }
//        }

//        pl.tymiwojts.Main.board[move.getRow() + 1][move.getColumn() + 1] = 2;
//        ctx.json("OK");
//        ctx.status(200);
    }

    private static int[][] getBoardState() {
        return board.getBoardState();
    }
}