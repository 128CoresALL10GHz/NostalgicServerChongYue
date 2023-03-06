import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Main {
    // 三技能固定动画时长+7次完整普通攻击+第八次抬手为213帧，为7100毫秒，二倍速下为3050毫秒
    private static final int SKILL_INACTIVE_NO_TALENT_PERIOD = 3050;

    // 三技能固定动画时长+4次完整普通攻击+第五次抬手为138帧，为4600毫秒，二倍速下为2300毫秒
    private static final int SKILL_INACTIVE_TALENT_PERIOD = 2300;

    // 三技能固定动画时长+3次完整普通攻击+第四次普通攻击抬手为115帧，约为3833毫秒，二倍速下约为1916毫秒
    private static final int SKILL_ACTIVE_NO_TALENT_PERIOD = 1916;

    // 三技能固定动画时长+2次完整普通攻击+第三次普通攻击抬手为90帧，为3000毫秒，二倍速下为1500毫秒
    private static final int SKILL_ACTIVE_TALENT_PERIOD = 1500;

    public static void main(String[] args) {
        // 四个技能语音的AudioClip实例，v就是voice的简写，写着玩的就随便一点了
        AudioClip v1 = Dage_sVoice.getAudioClip("./voices/1.wav");
        AudioClip v2 = Dage_sVoice.getAudioClip("./voices/2.wav");
        AudioClip v3 = Dage_sVoice.getAudioClip("./voices/3.wav");
        AudioClip v4 = Dage_sVoice.getAudioClip("./voices/4.wav");

        // 四个文本
        String v1Text = "形不成形，意不在意，再去练练吧。";
        String v2Text = "千招百式在一息！";
        String v3Text = "劲发江潮落，气收秋毫平！";
        String v4Text = "你们解决问题，还是只会仰仗干戈吗？";

        // 懒得写异常处理了凑活用吧，不会真的有人乱写数值吧不会吧不会吧（）
        Scanner in = new Scanner(System.in);
        System.out.println("请输入希望大哥吵闹的次数（不小于1，最好5以上）：");
        int count = in.nextInt();

        // 使用随机数的方式随机四个语音的播放列表
        int[] playList = new int[count];
        for (int i = 0; i < count; i++) {
            playList[i] = (int)(Math.random() * 4 + 1);
        }

        // 使用随机布尔值的方式确定每次技能释放后是否触发二天赋的+3技力，由于最后一次并不需要所以是count-1
        // 修改概率改数值就行，Math.random()生成数值区间是[0, 1)，>=0.1的话就是90%，>=0.01的话就是99%，这里的概率是95%，11-6配塞爹双人几乎是放技能必击倒
        boolean[] isDefeatedEnemy = new boolean[count - 1];
        for (int i = 0; i < count - 1; i++) {
            isDefeatedEnemy[i] = Math.random() >= 0.05;
        }

        // 第一次播放不需要停止前一次，所以单独拿出来处理，后续播放都在循环里
        switch (playList[0]) {
            case 1 : v1.play(); System.out.print(v1Text); break;
            case 2 : v2.play(); System.out.print(v2Text); break;
            case 3 : v3.play(); System.out.print(v3Text); break;
            case 4 : v4.play(); System.out.print(v4Text); break;
        }

        for(int i = 1; i < count; i++) {
            try {
                // 按照持续接敌的情况等待下一次技能释放，少女祈祷中
                if (i < 5) {
                    if (isDefeatedEnemy[i - 1]) Thread.sleep(SKILL_INACTIVE_TALENT_PERIOD);
                    else Thread.sleep(SKILL_INACTIVE_NO_TALENT_PERIOD);
                } else {
                    if (isDefeatedEnemy[i - 1]) Thread.sleep(SKILL_ACTIVE_TALENT_PERIOD);
                    else Thread.sleep(SKILL_ACTIVE_NO_TALENT_PERIOD);
                }

                // 加入一定的随机等待
                Thread.sleep((long)(Math.random() * 2000));
            } catch (InterruptedException ignored) { }


            // 强制停止上一个语音的播放，精髓所在
            switch (playList[i - 1]) {
                case 1 : v1.stop(); break;
                case 2 : v2.stop(); break;
                case 3 : v3.stop(); break;
                case 4 : v4.stop(); break;
            }

            // 接下来的语音播放
            switch (playList[i]) {
                case 1 : v1.play(); System.out.print(v1Text); break;
                case 2 : v2.play(); System.out.print(v2Text); break;
                case 3 : v3.play(); System.out.print(v3Text); break;
                case 4 : v4.play(); System.out.print(v4Text); break;
            }
        }

        // 此处等待是为了播放完语音，如果直接结束程序是无法播放出最后一条语音的，语音最长的是作战3，4秒多，所以等待5秒
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) { }
    }
}

class Dage_sVoice {
    // 随便写了个工厂方法用来获取AudioClip实例
    public static AudioClip getAudioClip(String file) {
        try {
            return Applet.newAudioClip(new File(file).toURI().toURL());
        } catch (MalformedURLException ignored) { }
        return null;
    }
}
