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
    private static final String[] LEVELS = {"level1", "level2", "level3", "level4", "level5"};
    protected CopyOnWriteArrayList<Ball> gameBalls;
    protected CopyOnWriteArrayList<Brick> bricks;
    protected CopyOnWriteArrayList<PowerUP> powerUPs;
    protected CopyOnWriteArrayList<Bullet> bullets;
    protected Bite bite;
    private int panelWidth, panelHeight;
    private LevelGenerator lg;
    private int lifeCount, levelCounter, score;
    private ArrayList<BufferedImage> biteImages;
    private BufferedImage bulletImage, fireBallImage;
    private boolean gameLaunch;
    private boolean isGameOver;

    public GameField(ArrayList<BufferedImage> biteImages, BufferedImage bulletImage, BufferedImage fireBallImage, int panelWidth, int panelHeight) {
        this.gameBalls = new CopyOnWriteArrayList<>();
        this.bricks = new CopyOnWriteArrayList<>();
        this.powerUPs = new CopyOnWriteArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.bite = null;
        this.lg = null;
        this.biteImages = biteImages;
        this.bulletImage = bulletImage;
        this.fireBallImage = fireBallImage;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
        gameLaunch = true;
        isGameOver = false;

        levelCounter = 1;
        lifeCount = 25;
        init();

    }

    public void init() {
        MusicPlayer m = new MusicPlayer();

        Thread t = new Thread(m);
        t.run();
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
        bite = new Bite(panelWidth / 2 - biteWidth / 2, panelHeight - biteHeight - TOOLBARHEIGHT, biteImages.get(0), biteWidth, biteHeight);
        Ball ball = new Ball(panelWidth / 2 - 12.5, bite.y - 26, gameLaunch);
        ball.setFireBall(fireBallImage);
        gameBalls.add(ball);
    }

    public void updateGameField(KeyEvent keyEvent, KeyEvent spaceKeyEvent) {

        bite.keyEvent = keyEvent;
        bite.spaceKeyEvent = spaceKeyEvent;
        updateAllItems();
        collisionCheck();

    }

    private void updateAllItems() {

        boolean isSpace = false;
        for (Ball ball : gameBalls) {
            if (ball.isGlued()) {
                if (ball.isGlued()) {
                    gluedMove(ball);
                    isSpace = gluedEvent(ball);
                    if (gameLaunch) {
                        bite.setSticky(false);
                        gameLaunch = false;
                    }
                }
            }

            triplets(ball);
            ball.updateSprite();
        }
        if (isSpace) {
            if (bite.getGlueCounter() > 0) {
                bite.setGlueCounter(bite.getGlueCounter() - 1);
            } else {
                bite.setSticky(false);
            }
        }
        biteUpdate();

        for (PowerUP powerUP : powerUPs) {
            powerUP.updateSprite();
        }

        for (Bullet bullet : bullets) {
            bullet.updateSprite();
        }
    }

    private void biteUpdate() {
        bite.setBallPowerUpEffect(null);
        for (Bullet bullet : bullets) {
            bullet.updateSprite();
        }
        if (bite.isWeapon) {
            bite.image = biteImages.get(1);
            shooting();
        } else {
            bite.image = biteImages.get(0);
        }

        if (bite.isNewLife() || lifeCount < MAXLIVE) {
            lifeCount++;
            bite.setNewLife(false);
        }
        bite.updateSprite();

    }

    private void collisionCheck() {
        //ball collisions;
        for (Ball b : gameBalls) {
            brickCollision(b);
            if (b.isCollision(bite)) {
                glue(b);
            }
            isBorder(b);
        }
        //Bite collisions;
        //Bite Border collision
        isBorder(bite);
        //powerUP collision;
        for (PowerUP powerUP : powerUPs) {
            if (bite.isCollision(powerUP) || isBorder(powerUP)) {
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
        if (bite.isSticky() && !ball.isGlued()) {
            ball.setGlued(true);
        }
    }

    private void gluedMove(Ball ball) {
        if (ball.isGlued()) {
            ball.spdx = 0;
            ball.spdy = 0;
            ball.x = ball.x + bite.defX;
        }
    }

    private boolean gluedEvent(Ball ball) {
        if (bite.spaceKeyEvent != null && ball.isGlued() && bite.spaceKeyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            ball.setGlued(false);
            return true;
        }
        return false;
    }

    private void shooting() {
        if (bite.spaceKeyEvent != null && bite.spaceKeyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            if (bite.isWeapon) {
                Thread t = new Thread(new SoundEffectManager("weaponShooting"));
                t.run();
                bullets.add(new Bullet(bite.x, bite.y, bulletImage));
                bullets.add(new Bullet(bite.x + bite.width, bite.y, bulletImage));
                bite.setBulletCount(bite.getBulletCount() - 1);
                bite.keyEvent = null;
                if (bite.getBulletCount() < 0) {
                    bite.isWeapon = false;
                }
            }

        }
    }

    private void triplets(Ball ball) {
        if (bite.getBallPowerUpEffect() != null) {
            ball.setCurrPowerUpEffect(bite.getBallPowerUpEffect());
            if (bite.getBallPowerUpEffect() == Sprite.PowerUpEffect.TRIPLE) {
                Ball newBall = new Ball(ball.x, ball.y, gameLaunch);
                newBall.setAngle(ball.getAngle() + Math.random() * 90);
                gameBalls.add(newBall);
                newBall = new Ball(ball.x, ball.y, gameLaunch);
                newBall.setAngle(ball.getAngle() - Math.random() * 90);
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
        if (sprite instanceof Bite) {
            if (rectangle.intersectsLine(leftBorder)) {
                ((Bite) sprite).isLeftBorder = true;
            }
            if (rectangle.intersectsLine(rightBorder)) {
                ((Bite) sprite).isRightBorder = true;
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
            bite = new Bite(panelWidth / 2 - biteWidth / 2, panelHeight - biteHeight - TOOLBARHEIGHT, biteImages.get(0), biteWidth, biteHeight);
            Ball ball = new Ball(panelWidth / 2 - 12.5, bite.y - 26, gameLaunch);
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
}


