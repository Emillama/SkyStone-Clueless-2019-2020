package org.firstinspires.ftc.teamcode.All;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by Joshua on 9/9/2017.
 */

public class FourWheelMecanumDrivetrain {

    public enum Direction {
        FORWARD,
        RIGHT,
        BACKWARD,
        LEFT
    }
    public HardwareMap rw;

    LinearOpMode runningOpMode;

    double speedMultiplier = 0.75;

    // Constants used to adjust various parameters / characteristics of the drivetrain
    final double rotSpeed = 0.75;
    final double speedThreshold = 0.05;

    public FourWheelMecanumDrivetrain(HardwareMap rw) {
        this.rw = rw;
    }

    // region auto
    // Primary movement method for auto

    public void setRunningOpMode(LinearOpMode opMode) {
        this.runningOpMode = opMode;
    }

    public void AutoMove(double speed, double angle, double time) throws InterruptedException{
        MoveAngle(speed, angle, 0);
        Thread.sleep((long)(time * 1000));
        stop();
    }

    public void AutoMove(Direction direction, double speed, double time) throws InterruptedException{
        MoveCardinal(direction, (float)speed);
        Thread.sleep((long)(time * 1000));
        stop();
    }

    // "Dumb" turn, based on time
    public void turn(boolean clockwise, double speed, double seconds) throws InterruptedException{
        Rotate(clockwise, speed);
        Thread.sleep((long)(seconds * 1000));
        stop();
    }

    public void resetEncoders() {
        DcMotor.RunMode runMode = rw.frontLeft.getMode();
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMotorMode(runMode);
    }
    public boolean anyIsBusy() {
        return rw.backLeft.isBusy() || rw.backRight.isBusy() || rw.frontLeft.isBusy() || rw.frontRight.isBusy();
    }
    public void setPowerAll(double power) {
        rw.backLeft.setPower(power);
        rw.backRight.setPower(power);
        rw.frontLeft.setPower(power);
        rw.frontRight.setPower(power);
    }

    //endregion
    public void MoveCardinal(Direction direction, float speed) {
        switch (direction) {
            case FORWARD:
                rw.frontRight.setPower(speed);
                rw.frontLeft.setPower(speed);
                rw.backRight.setPower(speed);
                rw.backLeft.setPower(speed);
                break;
            case BACKWARD:
                rw.frontRight.setPower(-speed);
                rw.frontLeft.setPower(-speed);
                rw.backRight.setPower(-speed);
                rw.backLeft.setPower(-speed);
                break;
            case LEFT:
                rw.frontRight.setPower(speed);
                rw.frontLeft.setPower(-speed);
                rw.backRight.setPower(-speed);
                rw.backLeft.setPower(speed);
                break;
            case RIGHT:
                rw.frontRight.setPower(-speed);
                rw.frontLeft.setPower(speed);
                rw.backRight.setPower(speed);
                rw.backLeft.setPower(-speed);
                break;

        }
    }

    // Turns robot
    public void Rotate(boolean clockwise, double speed) {
        if (!clockwise) {
            setPower(rw.frontRight, speed);
            setPower(rw.frontLeft, -speed);
            setPower(rw.backRight, speed);
            setPower(rw.backLeft, -speed);
        }
        else {
            setPower(rw.frontRight, -speed);
            setPower(rw.frontLeft, speed);
            setPower(rw.backRight, -speed);
            setPower(rw.backLeft, speed);
        }
    }

    public void encoderRotate(double speed, double inches, boolean clockwise) {
        double counts = inches / (2 * PI * 2) * DriveConstant.WHEEL_ENCODER_COUNTS_PER_REVOLUTION;

        int backLeftStart = rw.backLeft.getCurrentPosition();
        int backRightStart = rw.backRight.getCurrentPosition();
        int frontLeftStart = rw.frontLeft.getCurrentPosition();
        int frontRightStart = rw.frontRight.getCurrentPosition();

        if (runningOpMode == null) {
            return;
        }
        Rotate(clockwise, speed);
        while (runningOpMode.opModeIsActive()) {
            int backLeft = rw.backLeft.getCurrentPosition();
            int backRight = rw.backRight.getCurrentPosition();
            int frontLeft = rw.frontLeft.getCurrentPosition();
            int frontRight = rw.frontRight.getCurrentPosition();

            int backLeftDiff = Math.abs(backLeft - backLeftStart);
            int backRightDiff = Math.abs(backRight - backRightStart);
            int frontLeftDiff = Math.abs(frontLeft - frontLeftStart);
            int frontRightDiff = Math.abs(frontRight - frontRightStart);

            double avg = (backLeftDiff + backRightDiff + frontLeftDiff + frontRightDiff) / 4;

            if (runningOpMode != null) {
                runningOpMode.telemetry.addData("Average", avg);
                runningOpMode.telemetry.addData("Target", counts);
                runningOpMode.telemetry.update();
            }

            if (avg >= counts) {
                break;
            }
        }
        stop();
    }
    public void gyroTurn(double angle, double power){
        double p_turn_coeff = 0.1;
        double error;
        double currentAngle = rw.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        while(currentAngle != angle){
            currentAngle = rw.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            error = getError(angle);
        }
    }
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - rw.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }
    public void encoderDrive(double power, double inches){
        double counts = inches / (2 * PI * 2) * DriveConstant.WHEEL_ENCODER_COUNTS_PER_REVOLUTION;
        int backLeftStart = rw.backLeft.getCurrentPosition();
        int backRightStart = rw.backRight.getCurrentPosition();
        int frontLeftStart = rw.frontLeft.getCurrentPosition();
        int frontRightStart = rw.frontRight.getCurrentPosition();

        if (runningOpMode == null) {
            return;
        }
        setPowerAll(power);

        while (runningOpMode.opModeIsActive()) {
            int backLeft = rw.backLeft.getCurrentPosition();
            int backRight = rw.backRight.getCurrentPosition();
            int frontLeft = rw.frontLeft.getCurrentPosition();
            int frontRight = rw.frontRight.getCurrentPosition();

            int backLeftDiff = Math.abs(backLeft - backLeftStart);
            int backRightDiff = Math.abs(backRight - backRightStart);
            int frontLeftDiff = Math.abs(frontLeft - frontLeftStart);
            int frontRightDiff = Math.abs(frontRight - frontRightStart);

            double avg = (backLeftDiff + backRightDiff + frontLeftDiff + frontRightDiff) / 4;

            if (runningOpMode != null) {
                runningOpMode.telemetry.addData("Average", avg);
                runningOpMode.telemetry.addData("Target", counts);
                runningOpMode.telemetry.update();
            }

            if (avg >= counts) {
                break;
            }
        }
        stop();
    }

    public void odometryStrafe(double power, double inches, boolean right){
        double counts = inches / (2 * PI * 2) * DriveConstant.WHEEL_ENCODER_COUNTS_PER_REVOLUTION;
        int sidewaysStart = rw.rightIntake.getCurrentPosition();

        if(!right)
            power = -power;

        rw.frontRight.setPower(-power);
        rw.frontLeft.setPower(power);
        rw.backRight.setPower(power);
        rw.backLeft.setPower(-power);

        while (runningOpMode.opModeIsActive()) {
            int sideways = rw.backLeft.getCurrentPosition();

            int sidewaysDiff = Math.abs(sideways - sidewaysStart);

            if (runningOpMode != null) {
                runningOpMode.telemetry.addData("Side", sidewaysDiff);
                runningOpMode.telemetry.addData("Target", counts);
                runningOpMode.telemetry.update();
            }

            if (sidewaysDiff >= counts) {
                break;
            }
        }
        stop();
    }
    // Primary movement methods

    /**
     *
     * @param speed The speed of the robot from -1 to 1
     * @param angle Angle (in radians) that the robot should go
     * @param turn Turning velocity
     */
    public void MoveAngle(double speed, double angle, double turn) {
        double vRot = turn;



        double desiredAngle = (angle) + Math.PI / 4;
        if (desiredAngle < 0) {
            desiredAngle = desiredAngle + 2 * Math.PI;
        }
        if (desiredAngle >= 2 * Math.PI) {
            desiredAngle = desiredAngle % (2 * Math.PI);
        }

        double intermediateSin = sin(desiredAngle);
        double intermediateCos = cos(desiredAngle);

        double leftfront = speed * (intermediateSin) + (vRot * rotSpeed / speedMultiplier);
        double leftBackward = speed * (intermediateCos) + (vRot * rotSpeed / speedMultiplier);
        double rightfront = speed * (intermediateCos) - (vRot * rotSpeed / speedMultiplier);
        double rightBackward = speed * (intermediateSin) - (vRot * rotSpeed / speedMultiplier);

        if (Math.abs(rightBackward) < speedThreshold) {
            rightBackward = 0;
        }
        if (Math.abs(rightfront) < speedThreshold) {
            rightfront = 0;
        }
        if (Math.abs(leftBackward) < speedThreshold) {
            leftBackward = 0;
        }
        if (Math.abs(leftfront) < speedThreshold) {
            leftfront = 0;
        }
        setPower(rw.frontRight, rightfront);
        setPower(rw.frontLeft, leftfront);
        setPower(rw.backRight, rightBackward);
        setPower(rw.backLeft, leftBackward);
    }

    public void setPower(DcMotor motor, double speed) {
        motor.setPower((speed * speedMultiplier));
    }

    // Ceases all movement
    public void stop() {
        rw.frontRight.setPower(0);
        rw.frontLeft.setPower(0);
        rw.backRight.setPower(0);
        rw.backLeft.setPower(0);
    }

    // Blanket sets all zero power behaviours for the entire drivetrain
    public void setMotorZeroPower(DcMotor.ZeroPowerBehavior zeroPower) {
        rw.frontRight.setZeroPowerBehavior(zeroPower);
        rw.frontLeft.setZeroPowerBehavior(zeroPower);
        rw.backRight.setZeroPowerBehavior(zeroPower);
        rw.backLeft.setZeroPowerBehavior(zeroPower);
    }

    public void setMotorMode(DcMotor.RunMode runMode) {
        rw.frontRight.setMode(runMode);
        rw.frontLeft.setMode(runMode);
        rw.backRight.setMode(runMode);
        rw.backLeft.setMode(runMode);
        rw.leftIntake.setMode(runMode);
        rw.liftTwo.setMode(runMode);
        rw.rightIntake.setMode(runMode);
    }

    // Sets the "overall" speed of the drivetrain
    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public void displayInformation() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();

        dashboard.sendTelemetryPacket(packet);
    }

    double normalize(double angle) {
        angle = (360 + angle % 360) % 360;
        return angle;
    }

}
