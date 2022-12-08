package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class Driving358 extends LinearOpMode {


    Hardware358 robot = new Hardware358();
int hi=8;
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 537.6 ;    // eg: REV Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.25);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double     INCHES_FOR_RIGHT_ANGLE  = 4;

    static final double     LIFT_COUNTS_FULL_REVOLVE= 1440 /4;

    final double DESIRED_DISTANCE = 8.0; //  this is how close the camera should get to the target (inches)
    //  The GAIN constants set the relationship between the measured position error,
    //  and how much power is applied to the drive motors.  Drive = Error * Gain
    //  Make these values smaller for smoother control.
    final double SPEED_GAIN =   0.02 ;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN  =   0.01 ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MM_PER_INCH = 25.40 ;   //  Metric conversion

    public void motorStop() {
        robot.lf.setPower(0);
        robot.rf.setPower(0);
        robot.lb.setPower(0);
        robot.rb.setPower(0);
        robot.lift.setPower(0);

        //robot.rotateLeft.setPower(0);
        //robot.rotateRight.setPower(0);
//        robot.carousel.setPower(0);
        reset();
    }
    public void reset(){
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //.m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
   //     robot.rotateRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //     robot.rotateRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
   }
   public void runToPosition(){
       robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
   }
   public void runUsingEncoders()
   {
       robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
   }


//
    public void lift(int direction, String position, double power,int remaining){
        int tickConversion = 69; //How many ticks per 1cm of string pulled
        int cmMove = 0;

        if (position.equals("low")){
            cmMove  = 35;
        }
        else if (position.equals("mid")){
            cmMove  = 60;
        }
        else if (position.equals("high")){
            cmMove  = 85;
        }
        else if (position.equals("sStation")){
            cmMove  = 6 -(-remaining*5);
        }
        else if (position.equals("ground")){
            cmMove = 2;
        }

        int ticks = tickConversion * cmMove * direction;
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setTargetPosition((ticks));
        robot.lift.setPower(power);

    }
    public void move(double power, char direction, double distance){
        double ticks = COUNTS_PER_INCH * distance/3;
//        double ticks = 7.5* distance;
        switch(direction){
            case 'f':
                //to go forward

                //set target position
                robot.lf.setTargetPosition((int)(ticks));
                robot.lb.setTargetPosition((int)(ticks));
                robot.rf.setTargetPosition((int)(ticks));
                robot.rb.setTargetPosition((int)(ticks));
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(power);
                robot.rf.setPower(power);
                robot.lb.setPower(power);
                robot.rb.setPower(power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy())
                {

                }
                motorStop();
                runUsingEncoders();
                break;
            case 'b':
                //setting power of motors to go backward
                //set target position
                robot.lf.setTargetPosition((int) -ticks);
                robot.lb.setTargetPosition((int) -ticks);
                robot.rf.setTargetPosition((int) -ticks);
                robot.rb.setTargetPosition((int) -ticks);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(-power);
                robot.rf.setPower(-power);
                robot.lb.setPower(-power);
                robot.rb.setPower(-power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy())
                {
                    telemetry.clear();
                    telemetry.addData("Front Left Pos", robot.lf.getCurrentPosition());
                    telemetry.addData("Front Right Pos", robot.rf.getCurrentPosition());
                    telemetry.addData("Back Left Pos", robot.lb.getCurrentPosition());
                    telemetry.addData("Back Right (Mephistopheles) Pos", robot.rb.getCurrentPosition());
                    telemetry.update();

                }
                motorStop();
                runUsingEncoders();
                break;

            case 'r':
                //to strafe right


                //set target position
                robot.lf.setTargetPosition((int) ticks);
                robot.lb.setTargetPosition((int)-ticks);
                robot.rf.setTargetPosition((int)-ticks);
                robot.rb.setTargetPosition((int) ticks);
                //set run to position
                runToPosition();
                //set drive power for forward
                robot.lf.setPower(power);
                robot.rf.setPower(-power);
                robot.lb.setPower(-power);
                robot.rb.setPower(power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy())
                {

                }
                motorStop();
                runUsingEncoders();
                break;
            case 'l' :
                // to strafe left

                //set target position
                robot.lf.setTargetPosition((int)-ticks);
                robot.lb.setTargetPosition((int)ticks);
                robot.rf.setTargetPosition((int)ticks);
                robot.rb.setTargetPosition((int)-ticks);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(-power);
                robot.rf.setPower(power);
                robot.lb.setPower(power);
                robot.rb.setPower(-power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy())
                {

                }
                motorStop();
                runUsingEncoders();
                break;

            default:
                motorStop();
        }
    }

    public void rotate(double power, char direction, double angle) {
        double ticks = COUNTS_PER_INCH * angle / 90 * INCHES_FOR_RIGHT_ANGLE;
//        double ticks = 7.5* distance;
        switch(direction){
            case 'r':
                //to turn clockwise
                robot.lf.setTargetPosition((int)ticks);
                robot.lb.setTargetPosition((int)ticks);
                robot.rf.setTargetPosition((int)-ticks);
                robot.rb.setTargetPosition((int)-ticks);
                //set run to position
                runToPosition();
                //set drive power for forward
                robot.lf.setPower(power);
                robot.rf.setPower(-power);
                robot.lb.setPower(power);
                robot.rb.setPower(-power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy())
                {

                }
                motorStop();
                runUsingEncoders();
                break;
            case 'l':
                // to turn counter clockwise
                robot.lf.setTargetPosition((int)-ticks);
                robot.lb.setTargetPosition((int) -ticks);
                robot.rf.setTargetPosition((int)ticks);
                robot.rb.setTargetPosition((int) ticks);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(-power);
                robot.rf.setPower(power);
                robot.lb.setPower(-power);
                robot.rb.setPower(power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy())
                {
                    telemetry.clear();
                    telemetry.addData("Front Left Pos", robot.lf.getCurrentPosition());
                    telemetry.addData("Front Right Pos", robot.rf.getCurrentPosition());
                    telemetry.addData("Back Left Pos", robot.lb.getCurrentPosition());
                    telemetry.addData("Back Right (Mephistopheles) Pos", robot.rb.getCurrentPosition());
                    telemetry.update();
                }
                motorStop();
                runUsingEncoders();
                break;
            default:
                motorStop();
        }
    }
    public void diagonal(double power, char direction, long distance){
        double ticks = 1120/7.5 * distance;
        switch(direction) {
            case '1':
                //forward right

                //set target position

                robot.lf.setTargetPosition((int) (ticks));
                robot.lb.setTargetPosition(0);
                robot.rf.setTargetPosition(0);
                robot.rb.setTargetPosition((int) ticks);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(power);
                robot.rf.setPower(0);
                robot.lb.setPower(0);
                robot.rb.setPower(power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy()) {

                }
                motorStop();
                runUsingEncoders();
                break;

            case '2':
                //forward left

                //set target position

                robot.lf.setTargetPosition(0);
                robot.lb.setTargetPosition((int) ticks);
                robot.rf.setTargetPosition((int) ticks);
                robot.rb.setTargetPosition(0);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(0);
                robot.rf.setPower(power);
                robot.lb.setPower(power);
                robot.rb.setPower(0);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy()) {

                }
                motorStop();
                runUsingEncoders();
                break;
            case '3':
                // go back right

                robot.lf.setTargetPosition(0);
                robot.lb.setTargetPosition((int) -ticks);
                robot.rf.setTargetPosition((int) -ticks);
                robot.rb.setTargetPosition(0);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(0);
                robot.rf.setPower(-power);
                robot.lb.setPower(-power);
                robot.rb.setPower(0);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy()) {

                }
                motorStop();
                runUsingEncoders();


                break;

            case '4':
//back left
                robot.lf.setTargetPosition((int) -ticks);
                robot.lb.setTargetPosition(0);
                robot.rf.setTargetPosition(0);
                robot.rb.setTargetPosition((int) -ticks);
                //set run to position
                runToPosition();

                //set drive power for forward
                robot.lf.setPower(-power);
                robot.rf.setPower(0);
                robot.lb.setPower(0);
                robot.rb.setPower(-power);

                while (robot.lf.isBusy() && robot.lb.isBusy() && robot.rf.isBusy() && robot.rb.isBusy()) {

                }
                motorStop();
                runUsingEncoders();


                break;
            default:
                motorStop();

        }
    }

}
