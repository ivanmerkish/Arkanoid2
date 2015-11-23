import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameField Class: Game engine
 */
public class GameField {

    private static final int TOOLBARHEIGHT = 10;
    private static final int MAXLIVE = 26;
    private static final String[] LEVELS = {"level1", "level2", "level3", "level4"};
    protected CopyOnWriteArrayList<Ball> gameBalls;
    protected CopyOnWriteArrayList<Brick> bricks;
    protected CopyOnWriteArrayList<PowerUP> powerUPs;
    protected CopyOnWriteArrayList<Bullet> bullets;
    protected Paddle paddle;
    private boolean startNext;
    private int panelWidth, panelHeight;
    private LevelGenerator lg;
    private int lifeCount, levelCounter, score;
    private ArrayList<BufferedImage> biteImages;
    private BufferedImage bulletImage, fireBallImage;
    private boolean gameLaunch;
    private boolean isGameOver;
    private boolean isLevelComplete;
    private boolean isGameWon;

    public GameField(ArrayList<BufferedImage> biteImages, BufferedImage bulletImage, BufferedImage fireBallImage, int panelWidth, int panelHeight) {
        this.gameBalls = new CopyOnWriteArrayList<>();
        this.bricks = new CopyOnWriteArrayList<>();
        this.powerUPs = new CopyOnWriteArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.paddle = null;
        this.lg = null;
        this.biteImages = biteImages;
        this.bulletImage = bulletImage;
        this.fireBallImage = fireBallImage;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
        startNext = false;
        gameLaunch = true;
        isGameOver = false;
        isLevelComplete = false;
        isGameWon = false;

        levelCounter = 0;
        lifeCount = 3;
        init();

    }

    public void init() {
        isLevelComplete = false;
        lg = new LevelGenerator(LEVELS[levelCounter]);
        lg.setPanelHeight(panelHeight);
        lg.setPanelWidth(panelWidth);
        try {
            bricks = lg.buildLevel();
        } catch (IOException e) {
            System.out.println("Level Build Error");
        }
        int biteWidth, biteHeight;
        biteWidth = bricks.get(0).width * 3;
        biteHeight = bricks.get(0).height;
        paddle = new Paddle(panelWidth / 2 - biteWidth / 2, panelHeight - biteHeight - TOOLBARHEIGHT, biteImages.get(0), biteWidth, biteHeight);
        Ball ball = new Ball(panelWidth / 2 - 12.5, paddle.y - 26, gameLaunch);
        ball.setFireBall(fireBallImage);
        if (gameBalls.size() != 0) {
            gameBalls.clear();
        }
        gameBalls.add(ball);

    }

    public void updateGameField(KeyEvent keyEvent, KeyEvent spaceKeyEvent) {

        if (startNext) {
            if (levelCounter < LEVELS.length) {
                levelCounter++;
                init();
                startNext = false;
            } else {
                isGameWon = true;
            }
        }
        paddle.keyEvent = keyEvent;
        paddle.spaceKeyEvent = spaceKeyEvent;
        updateAllItems();
        collisionCheck();

    }

    private void updateAllItems() {
        if (bricks.size() == 0) {
            isLevelComplete = true;
        } else {
            boolean isSpace = false;
            for (Ball ball : gameBalls) {
                if (ball.isGlued()) {
                    if (ball.isGlued()) {
                        gluedMove(ball);
                        isSpace = gluedEvent(ball);
                        if (gameLaunch) {
                            paddle.setSticky(false);
                            gameLaunch = false;
                        }
                    }
                }

                triplets(ball);
                ball.updateSprite();
            }
            if (isSpace) {
                if (paddle.getGlueCounter() > 0) {
                    paddle.setGlueCounter(paddle.getGlueCounter() - 1);
                } else {
                    paddle.setSticky(false);
                }
            }
            paddleUpdate();

            for (PowerUP powerUP : powerUPs) {
                powerUP.updateSprite();
            }

            for (Bullet bullet : bullets) {
                bullet.updateSprite();
            }
        }
    }

    private void paddleUpdate() {
        paddle.setBallPowerUpEffect(null);
        for (Bullet bullet : bullets) {
            bullet.updateSprite();
        }
        if (paddle.isWeapon) {
            paddle.image = biteImages.get(1);
            shooting();
        } else {
            paddle.image = biteImages.get(0);
        }

        if (paddle.isNewLife() && lifeCount < MAXLIVE) {
            lifeCount++;
            paddle.setNewLife(false);
        }
        paddle.updateSprite();

    }

    private void collisionCheck() {
        //ball collisions;
        for (Ball b : gameBalls) {
            brickCollision(b);
            if (b.isCollision(paddle)) {
                glue(b);
            }
            isBorder(b);
        }
        //Paddle collisions;
        //Paddle Border collision
        isBorder(paddle);
        //powerUP collision;
        for (PowerUP powerUP : powerUPs) {
            if (paddle.isCollision(powerUP) || isBorder(powerUP)) {
                powerUPs.remove(powerUP);
            }
        }

        //Bullets Collisions
        for (Bullet bullet : bullets) {
            for (Brick brick : bricks) {
                if (bullet.isCollision(brick)) {
                    if (brickRemove(brick)) {
                        bullets.remove(bullet);
                        break;
                    }
                }
            }
        }
    }

    private void brickCollision(Ball b) {
        for (Brick brick : bricks) {
            if (b.isCollision(brick)) {
                if (b.isFireBall()) {
                    if (brick.getPowerUP() != null) {
                        powerUPs.add(brick.getPowerUP());
                        Thread t = new Thread(new SoundEffectManager("brickExplosionEvent"));
                        t.run();
                    }
                    bricks.remove(brick);
                    score += 100;
                } else {
                    if (brickRemove(brick)) {
                        break;
                    }
                }
            }
        }
    }

    private void glue(Ball ball) {
        if (paddle.isSticky() && !ball.isGlued()) {
            ball.setGlued(true);
        }
    }

    private void gluedMove(Ball ball) {
        if (ball.isGlued()) {
            ball.spdx = 0;
            ball.spdy = 0;
            ball.x = ball.x + paddle.defX;
        }
    }

    private boolean gluedEvent(Ball ball) {
        if (paddle.spaceKeyEvent != null && ball.isGlued() && paddle.spaceKeyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            ball.setGlued(false);
            return true;
        }
        return false;
    }

    private void shooting() {
        if (paddle.spaceKeyEvent != null && paddle.spaceKeyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            if (paddle.isWeapon) {
                Thread t = new Thread(new SoundEffectManager("weaponShooting"));
                t.run();
                bullets.add(new Bullet(paddle.x, paddle.y, bulletImage));
                bullets.add(new Bullet(paddle.x + paddle.width - bulletImage.getWidth(), paddle.y, bulletImage));
                paddle.setBulletCount(paddle.getBulletCount() - 1);
                paddle.keyEvent = null;
                if (paddle.getBulletCount() < 0) {
                    paddle.isWeapon = false;
                }
            }

        }
    }

    private void triplets(Ball ball) {
        if (paddle.getBallPowerUpEffect() != null) {
            ball.setCurrPowerUpEffect(paddle.getBallPowerUpEffect());
            if (paddle.getBallPowerUpEffect() == Sprite.PowerUpEffect.TRIPLE) {
                Ball newBall = new Ball(ball.x, ball.y, gameLaunch);
                newBall.setAngle(ball.getAngle() + Math.random() * 90);
                newBall.setGlued(ball.isGlued());
                newBall.setFireBall(ball.isFireBall());
                gameBalls.add(newBall);
                newBall = new Ball(ball.x, ball.y, gameLaunch);
                newBall.setAngle(ball.getAngle() - Math.random() * 90);
                newBall.setGlued(ball.isGlued());
                newBall.setFireBall(ball.isFireBall());
                gameBalls.add(newBall);
            }
        }
    }

    private boolean isBorder(Sprite sprite) {
        Line2D upperBorder = new Line2D.Double(15, 0, panelWidth, 0);
        Line2D leftBorder = new Line2D.Double(15, 0, 15, panelHeight);
        Line2D rightBorder = new Line2D.Double(panelWidth, 15, panelWidth, panelHeight);
        Line2D bottomBorder = new Line2D.Double(15, panelHeight, panelWidth, panelHeight);
        Rectangle2D rectangle = new Rectangle.Double(sprite.x, sprite.y, sprite.width, sprite.height);
        if (sprite instanceof Ball) {
            if (rectangle.intersectsLine(upperBorder)) {
                while (sprite.y < 15) {
                    sprite.y++;
                }
                ((Ball) sprite).setAngle(180 - ((Ball) sprite).getAngle());
                Thread t = new Thread(new SoundEffectManager("ballCollision"));
                t.run();
                return true;
            }
            if (rectangle.intersectsLine(bottomBorder)) {
                if (gameBalls.size() == 1) {
                    Thread t = new Thread(new SoundEffectManager("lifeLooseEvent"));
                    t.run();
                    lifeCount--;
                    restart();
                } else {
                    Thread t = new Thread(new SoundEffectManager("lostBallEvent"));
                    t.run();
                    gameBalls.remove(sprite);
                }

            }
            if (rectangle.intersectsLine(leftBorder) || rectangle.intersectsLine(rightBorder)) {
                while (sprite.x < 15) {
                    sprite.x++;
                }
                while (sprite.x > panelWidth) {
                    sprite.x--;
                }
                ((Ball) sprite).setAngle(360 - ((Ball) sprite).getAngle());
                Thread t = new Thread(new SoundEffectManager("ballCollision"));
                t.run();
                return true;
            }
        }
        if (sprite instanceof PowerUP) {

            if (rectangle.intersectsLine(bottomBorder)) {
                return true;
            }
        }
        if (sprite instanceof Paddle) {
            if (rectangle.intersectsLine(leftBorder)) {
                ((Paddle) sprite).isLeftBorder = true;
            }
            if (rectangle.intersectsLine(rightBorder)) {
                ((Paddle) sprite).isRightBorder = true;
            }
        }
        return false;
    }

    private void restart() {
        if (lifeCount - 1 > -1) {
            gameLaunch = true;
            gameBalls.clear();
            int biteWidth, biteHeight;
            biteWidth = bricks.get(0).width * 3;
            biteHeight = bricks.get(0).height;
            paddle = new Paddle(panelWidth / 2 - biteWidth / 2, panelHeight - biteHeight - TOOLBARHEIGHT, biteImages.get(0), biteWidth, biteHeight);
            Ball ball = new Ball(panelWidth / 2 - 12.5, paddle.y - 26, gameLaunch);
            ball.setFireBall(fireBallImage);
            gameBalls.add(ball);
        } else isGameOver = true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getScore() {
        return score;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public boolean isGameLaunch() {
        return gameLaunch;
    }

    public boolean brickRemove(Brick brick) {
        if (brick.getHardness() - 1 == 0) {
            if (brick.getPowerUP() != null) {
                powerUPs.add(brick.getPowerUP());
            }
            Thread t = new Thread(new SoundEffectManager("brickExplosionEvent"));
            t.run();
            bricks.remove(brick);
            score += 100;
            return true;
        } else if (brick.getHardness() - 1 > 0) {
            brick.setHardness(brick.getHardness() - 1);
            score += 50;
            Thread t = new Thread(new SoundEffectManager("ball2brickCollision"));
            t.run();
        } else {
            brick.setHardness(-1);
            Thread t = new Thread(new SoundEffectManager("ball2brickCollision"));
            t.run();
        }
        return false;
    }

    public boolean isLevelComplete() {
        return isLevelComplete;
    }

    public void setLevelComplete(boolean levelComplete) {
        isLevelComplete = levelComplete;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public void setStartNext(boolean startNext) {
        this.startNext = startNext;
    }
}


