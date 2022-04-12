/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;


// импорт библиотек
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author shtevnina
 */
public class Game extends JFrame { //класс Game наследует от класса JFrame
    
    private static Game game_window; //переменная, хранящая объект окна
    private static long last_frame_time; //переменная времени
    private static Image background; //объявление переменной для картинки
    private static Image game_over; //объявление переменной для картинки
    private static Image drop; //объявление переменной для картинки
    private static float drop_left = 200; //координата точки угла падающего объекта
    private static float drop_top = -100; //координата точки угла падающего объекта
    private static float drop_v = 200; //переменная скорости
    private static int score = 0; //переменная очков
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException { //обработка исключения
        // TODO code application logic here
        
        background = ImageIO.read(Game.class.getResourceAsStream("cosmos.jpg")); //загрузка картинки
        game_over = ImageIO.read(Game.class.getResourceAsStream("gameover.jpg")); //загрузка картинки
        drop = ImageIO.read(Game.class.getResourceAsStream("kosmonavt.jpg")); //загрузка картинки
        
        game_window = new Game(); //создание объекта окна
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //завершение программы при закрытии окна
        game_window.setLocation(200, 50); //точка появления окна программы
        game_window.setSize(900, 600); //размер окна
        game_window.setResizable(false); //запрет изменение размера окна
        last_frame_time = System.nanoTime(); //присвоение значения времени переменной
        GameField game_field = new GameField(); //создание объекта класса GameField
        game_field.addMouseListener(new MouseAdapter() { //обработчик события нажатия на кнопку мыши
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                
                if (is_drop) {
                    drop_top = -100; //координата точки угла падающего объекта
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null))); //координата точки угла падающего объекта
                    drop_v = drop_v + 10; //увеличение переменной скорости 
                    score++; //увеличение переменной очка
                    game_window.setTitle("Score: " + score); //отражение количества очков
                }
            }
        });
        game_window.add(game_field); //добавление в окно
        game_window.setVisible(true); //видимость окна
        
        
    }
    
    private static void onRepaint(Graphics g){ //метод для возможности рисования в окне (игровой цикл)
        long current_time = System.nanoTime();
        float delt_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        
        drop_top = drop_top + drop_v * delt_time;
        g.drawImage(background, 0, 0, null); //отрисовка картинки
        g.drawImage(drop, (int) drop_left, (int) drop_top, null); //отрисовка картинки
        if(drop_top > game_window.getHeight()) g.drawImage(game_over, 210, 150, null); //отрисовка картинки
    }
    
    private static class GameField extends JPanel { //создания класса, наследующего от класса JPanel
        
        @Override //динамическое замещение методов
        protected void paintComponent(Graphics g) { //переопределенный метод
            super.paintComponent(g); //получение доступа к компоненту 
            onRepaint(g); //вызов метода onRepaint
            repaint(); //отрисовка
        }
    }
    
}
