package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.BaseTrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.All.HardwareMap;
import org.firstinspires.ftc.teamcode.Autonomous.Vision.Align;
import org.firstinspires.ftc.teamcode.PID.DriveConstantsPID;
import org.firstinspires.ftc.teamcode.PID.mecanum.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.TeleOp.TeleopConstants;

import java.util.List;

public class Path {
    private Pose2d startingPos;
    private SampleMecanumDriveBase drive;
    private BaseTrajectoryBuilder builder;
    private Trajectory trajectory;
    private Align align;
    private HardwareMap hwMap;
    private OpMode opMode;
    private List<Recognition> tfod;

    public Path(HardwareMap hwMap, LinearOpMode opMode, SampleMecanumDriveBase drive, Pose2d startingPos){
        this.drive = drive;
        this.startingPos = startingPos;
        this.hwMap = hwMap;
        this.opMode = opMode;
        align = new Align(hwMap, opMode, DcMotor.ZeroPowerBehavior.BRAKE);
        this.drive.getLocalizer().setPoseEstimate(startingPos);
        this.drive.getLocalizer().update();
    }

    public void RedQuary(int[] skystonePositions){
        switch(skystonePositions[0]){
            case 1:
                transferReset();
                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);

                builder = builder.lineTo(new Vector2d(drive.getLocalizer().getPoseEstimate().getX(), drive.getLocalizer().getPoseEstimate().getY() + 5))
                        .splineTo(new Pose2d(new Vector2d(-25.832, -39.672), Math.toRadians(90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-42.728, -39.672), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.lineTo(new Vector2d(-39.728, -11.52));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-39.728, -11.52), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-39.728, -42.672))
                        .setReversed(false).splineTo(new Pose2d(new Vector2d(-5.568, -21.44), Math.toRadians(0)))
                        .splineTo(new Pose2d(new Vector2d(52.488, -27.296), Math.toRadians(-90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                align.setPower(0.2, 0.2);
                align.foundation(FieldPosition.RED_QUARY);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(54.144, -16.128), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.splineTo(new Pose2d(new Vector2d(29.064, -59.72), Math.toRadians(220)))
                        .setReversed(true).lineTo(new Vector2d(68.0, -59.72)).setReversed(false);
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                try { Thread.sleep(200); } catch (Exception e){}

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, -59.72), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(55.0, -59.72)).strafeTo(new Vector2d(55.0, -36.44))
                        .lineTo(new Vector2d(-20.552, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);  //@TODO Change this values to fit new path \/\/
                drive.turnSync(Math.toRadians(-45));

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-20.552, -36.44), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(-28.92, -20.312));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-28.92, -20.312), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-24.552, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                transferReset();
                drive.turnSync(Math.toRadians(60)); //@TODO Change this values to fit new path /\/\

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-24.552, -36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(68.0, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, -36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(22.0, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);
                break;
            case 2:
                transferReset();
                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);

                builder = builder.lineTo(new Vector2d(drive.getLocalizer().getPoseEstimate().getX(), drive.getLocalizer().getPoseEstimate().getY() + 5))
                        .splineTo(new Pose2d(new Vector2d(-35.208, -39.672), Math.toRadians(90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-42.728, -39.672), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.lineTo(new Vector2d(-39.728, -11.52));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-39.728, -11.52), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-39.728, -42.672))
                        .setReversed(false).splineTo(new Pose2d(new Vector2d(-5.568, -21.44), Math.toRadians(0)))
                        .splineTo(new Pose2d(new Vector2d(52.488, -27.296), Math.toRadians(-90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                align.setPower(0.2, 0.2);
                align.foundation(FieldPosition.RED_QUARY);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(54.144, -16.128), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.splineTo(new Pose2d(new Vector2d(29.064, -59.72), Math.toRadians(220)))
                        .setReversed(true).lineTo(new Vector2d(68.0, -59.72)).setReversed(false);
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                try { Thread.sleep(200); } catch (Exception e){}

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, -59.72), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(55.0, -59.72)).strafeTo(new Vector2d(55.0, -36.44))
                        .lineTo(new Vector2d(-20.552, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);  //@TODO Change this values to fit new path \/\/
                drive.turnSync(Math.toRadians(-45));

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-20.552, -36.44), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(-28.92, -20.312));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-28.92, -20.312), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-24.552, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                transferReset();
                drive.turnSync(Math.toRadians(60)); //@TODO Change this values to fit new path /\/\

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-24.552, -36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(68.0, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, -36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(22.0, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);
                break;
            case 3:
                transferReset();
                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);

                builder = builder.lineTo(new Vector2d(drive.getLocalizer().getPoseEstimate().getX(), drive.getLocalizer().getPoseEstimate().getY() + 5))
                        .splineTo(new Pose2d(new Vector2d(-42.728, -39.672), Math.toRadians(90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-42.728, -39.672), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.lineTo(new Vector2d(-39.728, -11.52));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-39.728, -11.52), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-39.728, -42.672))
                        .setReversed(false).splineTo(new Pose2d(new Vector2d(-5.568, -21.44), Math.toRadians(0)))
                        .splineTo(new Pose2d(new Vector2d(52.488, -27.296), Math.toRadians(-90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                align.setPower(0.2, 0.2);
                align.foundation(FieldPosition.RED_QUARY);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(54.144, -16.128), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.splineTo(new Pose2d(new Vector2d(29.064, -59.72), Math.toRadians(220)))
                        .setReversed(true).lineTo(new Vector2d(68.0, -59.72)).setReversed(false);
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                try { Thread.sleep(200); } catch (Exception e){}

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, -59.72), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(55.0, -59.72)).strafeTo(new Vector2d(55.0, -36.44))
                        .lineTo(new Vector2d(-20.552, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);
                drive.turnSync(Math.toRadians(-45));

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-20.552, -36.44), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(-28.92, -20.312));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-28.92, -20.312), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-24.552, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                transferReset();
                drive.turnSync(Math.toRadians(60));

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-24.552, -36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(68.0, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, -36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(22.0, -36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);
                break;
        }
    }

    public void RedFoundation(){
        align.skystone();
        trajectory = builder.build();
        drive.followTrajectorySync(trajectory);
    }

    public void BlueQuary(int[] skystonePositions){
        switch(skystonePositions[0]){
            case 1:
                break;
            case 2:
                break;
            case 3:
                transferReset();
                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);

                builder = builder.lineTo(new Vector2d(drive.getLocalizer().getPoseEstimate().getX(), drive.getLocalizer().getPoseEstimate().getY() - 5))
                        .splineTo(new Pose2d(new Vector2d(-42.728, 39.672), Math.toRadians(90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-42.728, 39.672), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.lineTo(new Vector2d(-39.728, 11.52));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-39.728, 11.52), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-39.728, 42.672))
                        .setReversed(false).splineTo(new Pose2d(new Vector2d(-5.568, 21.44), Math.toRadians(0)))
                        .splineTo(new Pose2d(new Vector2d(52.488, 27.296), Math.toRadians(-90)));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                align.setPower(0.2, 0.2);
                align.foundation(FieldPosition.RED_QUARY);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(54.144, 16.128), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.splineTo(new Pose2d(new Vector2d(29.064, 59.72), Math.toRadians(220)))
                        .setReversed(true).lineTo(new Vector2d(68.0, 59.72)).setReversed(false);
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.foundationLock.setPosition(TeleopConstants.foundationLockUnlock);
                hwMap.transferLock.setPosition(TeleopConstants.transferLockPosPlatform);

                try { Thread.sleep(200); } catch (Exception e){}

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, 59.72), Math.toRadians(0)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(55.0, 59.72)).strafeTo(new Vector2d(55.0, -6.44))
                        .lineTo(new Vector2d(-20.552, 36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(1);
                drive.turnSync(Math.toRadians(-45));

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-20.552, 36.44), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(-28.92, 20.312));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                intake(0);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-28.92, 20.312), drive.getExternalHeading()));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(-24.552, 36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                transferReset();
                drive.turnSync(Math.toRadians(60));

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(-24.552, 36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(true).lineTo(new Vector2d(68.0, 36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);

                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosPush);
                try { Thread.sleep(300); } catch (Exception e){}
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosTucked);

                drive.getLocalizer().setPoseEstimate(new Pose2d(new Vector2d(68.0, 36.44), Math.toRadians(180)));
                drive.getLocalizer().update();
                builder = new TrajectoryBuilder(drive.getPoseEstimate(), DriveConstantsPID.BASE_CONSTRAINTS);
                builder = builder.setReversed(false).lineTo(new Vector2d(22.0, 36.44));
                trajectory = builder.build();
                drive.followTrajectorySync(trajectory);
                break;
        }
    }

    public void BlueFoundation(){
        trajectory = builder.build();
        drive.followTrajectorySync(trajectory);
    }

    public void updateTFODData(List<Recognition> tfod){
        this.tfod = tfod;
        updateTFOD();
    }

    public Pose2d getPoseEstimate(){
        return drive.getLocalizer().getPoseEstimate();
    }

    public double getExternalHeading(){
        return drive.getExternalHeading();
    }

    private void updateTFOD(){
        align.updateTFOD(tfod);
    }

    public void updateHeading(){
        align.updateExternalHeading(drive.getExternalHeading());
    }

    private void transferReset(){
        Thread thread = new Thread(){
            public void run(){
                hwMap.transferHorn.setPosition(TeleopConstants.transferHornPosReady);
                hwMap.innerTransfer.setPosition(TeleopConstants.innerTransferPosExtended);
            }
        };
        thread.start();
    }

    private void intake(double power) {
        Thread thread = new Thread() {
            public void run() {
                hwMap.leftIntake.setPower(-power);
                hwMap.rightIntake.setPower(power);
            }
        };
        thread.start();
    }
}
