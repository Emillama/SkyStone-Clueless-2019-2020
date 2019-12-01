package org.firstinspires.ftc.teamcode.PID.localizer;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.jetbrains.annotations.NotNull;

public class TrackingWheelLocalizerWithIMU implements Localizer {
    Pose2d poseEstimate = new Pose2d(0, 0, 0);
    StandardTrackingWheelLocalizer trackingWheelLocalizer;
    private BNO055IMU imu;
    public TrackingWheelLocalizerWithIMU(HardwareMap hardwareMap, BNO055IMU imu) {
        trackingWheelLocalizer = new StandardTrackingWheelLocalizer(hardwareMap);
        this.imu = imu;
    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {
        return poseEstimate;
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        trackingWheelLocalizer.setPoseEstimate(pose2d);
    }

    @Override
    public void update() {
        trackingWheelLocalizer.update();
        poseEstimate = new Pose2d(trackingWheelLocalizer.getPoseEstimate().getX(), trackingWheelLocalizer.getPoseEstimate().getY(),
                imu.getAngularOrientation().firstAngle);
    }
}