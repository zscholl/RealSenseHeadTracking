/*******************************************************************************

INTEL CORPORATION PROPRIETARY INFORMATION
This software is supplied under the terms of a license agreement or nondisclosure
agreement with Intel Corporation and may not be copied or disclosed except in
accordance with the terms of that agreement
Copyright(c) 2014 Intel Corporation. All Rights Reserved.

*******************************************************************************/

import intel.rssdk.*;
import java.lang.System.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;

public class FaceTracking {
    private static PXCMFaceData faceData;
    private static PXCMSenseManager senseMgr;
    private static PXCMFaceModule faceModule;
       
    public static void main(String s[]) throws java.io.IOException
    {
        Scanner stdin = new Scanner(System.in);
        boolean run = true;
        float[] calibrationParameters = new float[6];

      
                
      

        String[] inputReq = {"Max Pitch", "Min Pitch", "Max Roll", "Min Roll", "Max Yaw", "Min Yaw"};

        senseMgr = PXCMSenseManager.CreateInstance();
        
        senseMgr.EnableFace(null);
        
        faceModule = senseMgr.QueryFace();
                
        // Retrieve the input requirements
        pxcmStatus sts = pxcmStatus.PXCM_STATUS_DATA_UNAVAILABLE; 
        PXCMFaceConfiguration faceConfig = faceModule.CreateActiveConfiguration();
        faceConfig.SetTrackingMode(PXCMFaceConfiguration.TrackingModeType.FACE_MODE_COLOR);
        faceConfig.detection.isEnabled = true; 
        faceConfig.landmarks.isEnabled = true; 
        faceConfig.pose.isEnabled = true; 
        faceConfig.ApplyChanges();
        faceConfig.Update();
        
        senseMgr.Init();

        System.out.println("Begin calibration");
        for (int i = 0; i < 6; i++){
            System.out.println("Look to position " + inputReq[i] + " ....Enter any character when ready");
            while(!stdin.hasNext()){}
                stdin.nextLine();
            calibrationParameters[i] = grabData(i);
        }
        System.out.println(calibrationParameters);


        while (run)
        {
            senseMgr.AcquireFrame(true);
            
            PXCMCapture.Sample sample = senseMgr.QueryFaceSample();
            
            faceData = faceModule.CreateOutput();
            faceData.Update();
            
            // Read and print data 
            for (int fidx=0; ; fidx++) {
                PXCMFaceData.Face face = faceData.QueryFaceByIndex(fidx);
                if (face==null) break;
                PXCMFaceData.DetectionData detectData = face.QueryDetection(); 
              
                if (detectData != null)
                {
                    PXCMRectI32 rect = new PXCMRectI32();
                    boolean ret = detectData.QueryBoundingRect(rect);
                    if (ret) {
                        System.out.println("Face Detected!");
                    }
                } else 
                    break;
                
                PXCMFaceData.PoseData poseData = face.QueryPose();
                if (poseData != null)
                {
                    PXCMFaceData.PoseEulerAngles pea = new PXCMFaceData.PoseEulerAngles();
                    poseData.QueryPoseAngles(pea);
                    System.out.println ("(Roll, Yaw, Pitch) = (" + pea.roll + "," + pea.yaw + "," + pea.pitch + ")"); 
                }  
            }  
  
            faceData.close();
            senseMgr.ReleaseFrame();
        }
        
        senseMgr.Close();
        System.exit(0);
    } 
    /*
    * int choice -- 0/max pitch 1/min pitch 2/max Roll 3/min roll 4/max yaw 5/min yaw
    *
    */
    private static float grabData(int choice){

        


            float data;

            senseMgr.AcquireFrame(true);
            
            PXCMCapture.Sample sample = senseMgr.QueryFaceSample();
            
            faceData = faceModule.CreateOutput();
            faceData.Update();
            PXCMFaceData.Face face = faceData.QueryFaceByIndex(0);
                if (face==null) return new Float(0.0/0.0);
                PXCMFaceData.DetectionData detectData = face.QueryDetection(); 
              
                if (detectData != null)
                {
                    PXCMRectI32 rect = new PXCMRectI32();
                    boolean ret = detectData.QueryBoundingRect(rect);
                    if (ret) {
                        System.out.println("Face Detected!");
                    }
                } else 
                    return new Float(0.0/0.0);
                
                PXCMFaceData.PoseData poseData = face.QueryPose();
                if (poseData != null)
                {
                    PXCMFaceData.PoseEulerAngles pea = new PXCMFaceData.PoseEulerAngles();
                    poseData.QueryPoseAngles(pea);
                    System.out.println ("(Roll, Yaw, Pitch) = (" + pea.roll + "," + pea.yaw + "," + pea.pitch + ")"); 
                    switch(choice){
                        case 0: 
                        return (float)pea.pitch;
					case 1:
                        return (float)pea.pitch;
					case 2:
                        return (float)pea.roll;               
					case 3:
                        return (float)pea.roll;
					case 4:
                        return (float)pea.yaw;
					case 5:
                        return (float)pea.yaw;
					default:
                        return new Float(0.0/0.0);
                    }
                }  
                else { return new Float(0.0/0.0);}

    }
}
