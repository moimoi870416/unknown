package weapon;

public class Gun {
    private GunType gunType;

    public Gun(){

    }

    public enum GunType{
        PISTOL,
        UZI,
        AK,
        SNIPER_GUN,
        MACHINE_GUN

    }

    private void setGun(){
        switch (gunType){
            case PISTOL:
                atk = 11;
                MOVE_SPEED = 10;
                shootDeviation = setShootDeviation(-2,2);
                break;
            case UZI:
                atk = 19;
                MOVE_SPEED = 18;
                shootDeviation = setShootDeviation(-5,5);
                break;
            case AK:
                atk = 31;
                MOVE_SPEED = 14;
                shootDeviation = setShootDeviation(-3,3);
                break;
            case SNIPER_GUN:
                atk = 120;
                MOVE_SPEED = 20;
                shootDeviation = 1;
                break;
            case MACHINE_GUN:
                atk = 41;
                MOVE_SPEED = 14;
                shootDeviation = setShootDeviation(-7,7);
                break;
        }
    }
}
