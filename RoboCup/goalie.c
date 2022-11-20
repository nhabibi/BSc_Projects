/* -*- Mode: C++ -*- */
/* PlayerGoalie1.C
 * KEMPS@IUT  (soccer client for Robocup2002)
 * Computer Science Department
 * Isfahan University of Technology
 * Copyright (C) 2001
 */
#include "PlayerGoalie.h"
#include "math.h"
#include "client.h"
#define no_kick 0
#define my_pass_kick 1
#define pass_kick 2
#define shoot_kick 3
#define maybe_shoot_kick 4

#define MARK_DIST 3
#define MARK_RATIO 0.28 //(tan30)/2
//#include "Memory.h"
void
PlayerGoalie::Behave ()
{
	if (Mem->RM_My_Goal == Goal_R)
	{
		bottom = Flag_TR40;
		top = Flag_TR50;
		gfb = Flag_CT;
		gft = Flag_CB;
		goale_top = Flag_RT10;
	}
	else
	{
		bottom = Flag_TL40;
		top = Flag_TL50;
		gfb = Flag_LB;
		gft = Flag_LT;
		goale_top = Flag_LT10;
	}
//~ printf("\nCYCLE CATCH BALL -----------%d\n",CycleCatchedBall);
	if (first_set == 1)	//view already was seted
	{
	  if (Mem->MyConf ())
      {
		if (Mem->BallPositionValid ())
		{
			switch (Mem->PlayMode)
			{
			case PM_Before_Kick_Off:
				//~ printf ("\n\n\n***before kick of\n");
				MoveToInitPos ();
				shootcontinue = FALSE;
				OneToOneFlag = FALSE;
				break;
			case PM_Play_On:
				ActionListMan.EmptyActionList();
				ThinkPlayOn ();
				ResetCycles(&Play_On_Cycles);
				break;
    		case PM_My_Free_Kick:	
    		case PM_My_Offside_Kick:
				shootcontinue = FALSE;
				OneToOneFlag = FALSE;
				ResetCycles(&Free_Kick_Cycles);	
				if(Free_Kick_Cycles >= 140){
					if(Free_Kick_Cycles < 145)
						HLAction.face_neck_and_body_to_point(Vector(0,0));
					else
						Goal_Kick_Pass ();
					//~ HLAction.kick_ball(Vector(0,0),KM_Moderate,2,TURN_CLOSEST);
					I_Kicked = TRUE;
				}
				else
				{
 					if(Free_Kick_Cycles < 70)
						HLAction.go_to_point(Vector(-48,0));
					else
						DontKnowMyPosition();
				}
						Free_Kick_Cycles ++;
 				break;
			case PM_Their_Kick_Off:
    		case PM_Their_Offside_Kick:
    		case PM_Their_Kick_In:
  	    	case PM_Their_Free_Kick:
				Dawdle();
				GetMark();
			break;
			case PM_My_Kick_In:
			case PM_My_Kick_Off:
				shootcontinue = FALSE;
				OneToOneFlag = FALSE;
				Dawdle ();
				break;
			case PM_My_Goalie_Free_Kick:
				ResetCycles(&Goal_Kick_Cycles);
				if(Goal_Kick_Cycles >= 140){
					if(Goal_Kick_Cycles < 145)
						HLAction.face_neck_and_body_to_point(Vector(0,0));
					else
						Goal_Kick_Pass ();
					//~ HLAction.kick_ball(Vector(0,0),KM_Moderate,2,TURN_CLOSEST);
					I_Kicked = TRUE;
				}
				else
				{
					if(Goal_Kick_Cycles < 40)
						HLAction.go_to_point(Vector(-48,0));
					else
						if(Goal_Kick_Cycles ==70)
							move(Vector(-45, - 6));
						else
							if(Goal_Kick_Cycles == 40)
								move(Vector(-40,-3));
							else
								DontKnowMyPosition();
				}
				Goal_Kick_Cycles++;
				break;
			case PM_My_Goal_Kick:
				shootcontinue = FALSE;
				OneToOneFlag = FALSE;
				ResetCycles(&Goal_Kick_Cycles);
				//~ printf("\n______GOLE KICK __%d___MODE\n",Goal_Kick_Cycles);
				if(Goal_Kick_Cycles >= 140){
					if(Goal_Kick_Cycles < 145)
						HLAction.face_neck_and_body_to_point(Vector(0,0));
					else
						Goal_Kick_Pass ();
					//~ HLAction.kick_ball(Vector(0,0),KM_Moderate,2,TURN_CLOSEST);
					I_Kicked = TRUE;
				}
				else
				{
 					if(Goal_Kick_Cycles < 70)
						HLAction.go_to_point(Vector(-48,0));
					else
						DontKnowMyPosition();
				}
				Goal_Kick_Cycles ++;
				break;
			
			default:
				shootcontinue = FALSE;
				OneToOneFlag = FALSE;
				Dawdle();
				break;
			}
		}
		else
		{
			DontKnowBallPosition ();
		}
	  }
	  else
	  {
 		DontKnowMyPosition ();
 	  }	
 	}
	else
	{
		change_view (VQ_High, VW_Wide);
		first_set = 1;
	}
	//~ MessagesMan.Clear();
				//~ if(!MessagesMan.SayMarkMsg(5, 6))
					//~ Mem->LogAction2(150,"______SAY MESSAGE IS FULL");
				//~ else
					//~ Mem->LogAction2(150,"______paked mark MESSAGE IS created");
	MessagesMan.Say();
	if(ActionListMan.NotEmpty())
		ActionListMan.GetSelectedAction().Perform();
}
/*__________________________________ added by H & KH __________________________________////////////////////*/
void PlayerGoalie::ThinkPlayOn ()
{
	Unum opp,teammate1;
	if((opp =Mem->ClosestOpponentToBall() ) != Unum_Unknown)
	if(	!Mem->OpponentPositionValid(opp))
		opp =Unum_Unknown;
	if((teammate1=Mem->ClosestTeammateToBall() ) != Unum_Unknown)
	if(	!Mem->TeammatePositionValid(teammate1))	
		teammate1 = Unum_Unknown;
	OneToOneFlag = FALSE;	
	static int counter=0;
	if(Mem->ViewWidth == VW_Normal 
		&& !(Mem-> BallInOwnPenaltyArea() && Mem->BallX() < (Mem->SP_pitch_length/2 - Mem->SP_goal_area_length -3))){
	if((Mem->CurrentTime.t - LastSeenTimeFlagCB) > 6 ){
		HLAction.face_only_neck_to_point(Mem->MarkerPosition(Flag_CB));
		if(counter == 0)
			counter++;
		else{
			counter = 0;
		LastSeenTimeFlagCB = Mem->CurrentTime.t ;
		}
	}
	else
		if(Mem->CurrentTime.t - LastSeenTimeFlagCT > 6 ){
			HLAction.face_only_neck_to_point(Mem->MarkerPosition(Flag_CT));
			if(counter == 0)
				counter++;
			else{
				counter = 0;
				LastSeenTimeFlagCT  = Mem->CurrentTime.t ;
			}
		}
		else
			HLAction.face_only_neck_to_ball();//if(Mem->BallAbsolutePosition())
		}
	else	
		HLAction.face_only_neck_to_ball();
	
	Mem->LogAction4(150, "BallDistance %f  _____ BallAngle %f", Mem->BallDistance(),Mem->BallAngleFromBody());
	//~ if(Mem->BallX()< - Mem->SP_penalty_area_length - 5)
		//~ change_view(VW_Normal);
	//~ else
		//~ change_view(VW_Wide);
		static int mark_cycles =0;
	//~ HLAction.face_neck_to_ball();
	static int LastChangeViewTime=Mem->CurrentTime.t; 
	static int LastNarrowChangeView = Mem->CurrentTime.t;
	if(Mem->BallDistance() < Mem->SP_penalty_area_length+5)
		change_view(VQ_High,VW_Normal);
	if(I_Kicked)
		if (((Mem->TeamWithBall(0) == Mem->MySide && Mem->TeammateWithBall(0) != Mem->MyNumber)
			||	Mem->TeamWithBall(0)== Mem->TheirSide) )
			I_Kicked = FALSE;
		else
		{
		Goal_Kick_Pass();
			return ;
		}
	//~ if(Mem->CurrentTime.t - LastNarrowChangeView > 5)	{
			//~ LastNarrowChangeView = Mem->CurrentTime.t;
			//~ if(Mem->ViewWidth == VW_Narrow)
				//~ change_view(VQ_High,VW_Normal);
			//~ else
					//~ change_view(VQ_High,VW_Narrow);
		//~ }
	else
	if(Mem->ViewWidth == VW_Wide)
	{	if(Mem->CurrentTime.t - LastChangeViewTime > 8){
				LastChangeViewTime = Mem->CurrentTime.t;
				change_view(VQ_High,VW_Normal);
		}
	}
	else
		if(Mem->CurrentTime.t - LastChangeViewTime > 4){
				change_view(VQ_High,VW_Wide);
			LastChangeViewTime = Mem->CurrentTime.t;
		}
			
	static int catchPerformed = 0;
	if(OneToOneFlag &&(( OpponentWithBall != Mem-> ClosestOpponentToBall()) || Mem->BallSpeed() > 1 ))
		   OneToOneFlag = FALSE;
	OpponentWithBall = Mem->ClosestOpponentToBall();
	//~ HLAction.face_neck_to_ball();
	CycleCatchedBall = 0;
	if(!Mem->BallInOwnPenaltyArea() && I_Kicked)
		I_Kicked = FALSE;

	if(catchPerformed == 1){
		//~ printf("\n__TIME_%d______BALL ANGLE AFTER CATCH ___%f____________BAllDistance ___%f____\n",Mem->CurrentTime.t,Mem->BallAngleFromBody(),Mem-> BallDistance());
		catchPerformed = 0;
		}
			GetMark();
			BallGoalLine.LineFromTwoPoints(Mem-> MarkerPosition(Mem->RM_My_Goal),Mem->BallAbsolutePosition());
			GoalieLine = Line (1, 0,(-1 *(Mem->MarkerPosition (top).x + 1.5)));
			if (!Ifcare ())
			{
				Dawdle ();
			}
			//else shold care
			else if (!Mem->BallMoving ())
			{
				printf("\n_________BALL NOT MOVING !!!!!!!!!!!\n");
				if (Mem->BallX() > -30)
					Dawdle ();
				else
				{	//ball is in penalty area
				AngleDeg ang;
				if ((Mem->BallCatchable () || BallCatchableNextCycle(& ang)) && !I_Kicked)
				{
						OneToOneFlag = FALSE;
					//~ goforcatch = FALSE;
					if(Mem->BallCatchable())
						ang = GetCatchAngle();
					//~ printf ("\n\n\n_____________Ball catchable & moving\n");
					if (CycleCatchedBall == 0)
					{
					goalie_catch (ang);
						//~ printf ("\nTIME___%d______CATCH BALL__________Ball distance__%f\n", Mem->CurrentTime.t, Mem->BallDistance ());
						//~ printf ("\n_BALL ANGLE ______%f______ BALL SPEED__________%f\n", Mem->BallAngleFromBody (), Mem->BallSpeed ());
						++CycleCatchedBall;
						catchPerformed = 1;
					}
					else{
						//~ printf("\n ___BALL ___ CATCHABLE __ I CAN NOT CATCH BALL____\n");
						if (Mem->BallSpeed () > .7
							&& Mem-> InOwnPenaltyArea (Mem->BallPathInterceptPoint()) && Mem->BallPathInterceptCanIGetThere())
						{
							Vector point = 	Mem-> BallPathInterceptPoint ();
							Line line;
							line.LineFromTwoPoints(Mem->BallAbsolutePosition(),point);			
							point.x -= 2;
							point.y = line.get_y(point.x);			
							//~ point.x-= 1;
							HLAction.my_goalie_go_to_point(point, 1, 100);					
						}
						else
							Goal_Kick_Pass();
					}
				}
					else
						if(Mem->BallKickable())
							Goal_Kick_Pass();
						else
							if(Mem-> BallInOwnPenaltyArea())
								HLAction.my_goalie_go_to_point (Mem->BallAbsolutePosition(), 2,100);
				}	//in penalty           
			}
			else	//catchable onetoone else
			{
				AngleDeg ang;
				if ((Mem->BallCatchable () || BallCatchableNextCycle(& ang)) && !I_Kicked)
				{
					OneToOneFlag = FALSE;
					//~ goforcatch = FALSE;
					if(Mem->BallCatchable())
						ang = GetCatchAngle();
					//~ printf ("\n\n\n_____________Ball catchable & moving\n");
					if (CycleCatchedBall == 0)
					{
					goalie_catch (ang);
						//~ printf ("\nTIME___%d______CATCH BALL__________Ball distance__%f\n", Mem->CurrentTime.t, Mem->BallDistance ());
						//~ printf ("\n_BALL ANGLE ______%f______ BALL SPEED__________%f\n", Mem->BallAngleFromBody (), Mem->BallSpeed ());
						++CycleCatchedBall;
						catchPerformed = 1;
					}
					else{
						//~ printf("\n ___BALL ___ CATCHABLE __ I CAN NOT CATCH BALL____\n");
						//~ if (Mem->BallSpeed () > .7 
							//~ && Mem-> InOwnPenaltyArea (Mem->BallPathInterceptPoint()) && CanGetBall())//Mem->BallPathInterceptCanIGetThere())
						//~ {
							//~ Vector point = 	Mem-> BallPathInterceptPoint ();
							//~ Line line;
							//~ line.LineFromTwoPoints(Mem->BallAbsolutePosition(),point);
							//~ point.x -= 2;
							//~ point.y = line.get_y(point.x);
							//~ //	point.x-= 1;
							//~ HLAction.my_goalie_go_to_point(point, 1, 100);
						//~ }
						//~ else
							Goal_Kick_Pass();

					}
				}
				else
					if(Mem->BallKickable())
							Goal_Kick_Pass();
				else
					//~ if(Mem->BallInOwnPenaltyArea() && CanGetBall())//Mem->BallPathInterceptCanIGetThere())
					//~ {
						//~ Unum opp;
						//~ if((opp = Mem->ClosestOpponentToBall()) != Unum_Unknown)
						//~ {
							//~ if(Mem->OpponentDistanceToBall(opp) > Mem->BallDistance())
								//~ HLAction.goalie_go_to_point(Mem->BallPathInterceptPoint());
						//~ }
						//~ else
							//~ HLAction.goalie_go_to_point(Mem->BallPathInterceptPoint());
					//~ }
					//~ else
					if(shooted() == shoot_kick){
						printf("\n_________GET SHOOT _________\n");
						GetShoot();
					}
					else
					if(shooted() == pass_kick){
						printf("\n_________GET OPPONENT PASS_________\n");
						GetOpponentPass();
					}
					//~ else if (OneToOne () )//||    OneToOneFlag)Mem->BallInOwnGoalArea()||
					//~ {
					//~ printf("\n_________OME TO ONE_________\n");
						//~ OneToOneFlag = TRUE;
						//~ }
					//~ {

						//~ printf("\n_________GET OME TO ONE_________\n");
						//~ OneToOneFlag = TRUE;
						//~ getOneToOne ();
					//~ }
					else	
						if(opp != Unum_Unknown && Mem-> InOwnPenaltyArea()
							&& Mem-> OpponentDistanceToBall(opp) < Mem->SP_kickable_area+1 
							&& fabs(Mem->BallY()) < Mem->SP_goal_area_width
							&& Mem->OpponentX(opp) < -(Mem->SP_pitch_length/2 - Mem->SP_penalty_area_length-5) 
							//~ && (teammate1 == Unum_Unknown || Mem->TeammateDistanceToBall(teammate1) < 4)
							&&	(NumOpponentInPenaltyArea()< 2 || Mem->BallDistance() +1 < Mem->OpponentDistanceToBall(opp)))
						{
							//~ Line ball_line = Line(Ray(Mem->BallAbsolutePosition(),Mem->BallAbsoluteVelocity()));
							//~ if(ball_line.dist(Mem->MyPos()) < 1)
								HLAction.goalie_go_to_point(Mem->BallAbsolutePosition());	
							//~ else
							//~ {
								//~ Vector point ;
								//~ point.x = Mem->MyX();
								//~ point.y = ball_line.get_y(point.x);
								//~ HLAction.goalie_go_to_point(point);
							//~ }
						}
						//~ else
							//~ GoToNimsazAndLinePrj();
				else
					if(CanGetBall() && OneToOne() && NumOpponentInPenaltyArea()< 2){
						Line ball_line = Line(Ray(Mem->BallAbsolutePosition(),Mem->BallAbsoluteVelocity()));
						if(ball_line.dist(Mem->MyPos()) < 1.5)
							HLAction.goalie_go_to_point(Mem->BallAbsolutePosition());
						else
							HLAction.goalie_go_to_point(Mem->BallPathInterceptPoint());
					}
					else
					{
						printf("\n_________GO TO NIMSAz _________\n");
						GoToNimsazAndLinePrj();
						//~ printf("\n ELSE ELSE ELSE ELSE EKSE EKSE ELSE ELSE \n");
					}
			}	//ball moving
}
//__________________________________________________________________________________
void PlayerGoalie::GetMark()
{
	if(Mem->BallAbsolutePosition().x > -15 )
		return;
	//~ if(Mem->CurrentTime.t - LastSayMarkTime < 2)
		//~ return;
	static int counter=0 ;
	static int flag=0;
	int temp_counter =0;
	Unum teammate_num;
	int attackers[10], attack =0;
	int defenders[10], defend =0;
	int  def;
	int marked=0;
	Unum defenders_told_to_mark[10];  
	int  told_mark=0;
	for(int i=0;i<=9;i++)
		defenders_told_to_mark[i]=Unum_Unknown;

	//find attackers
	for (int i=1; i<=Mem->SP_team_size; ++i)
		if (Mem->OpponentPositionValid(i))
			//~ if(Mem-> OpponentX(i) < 0)// (Mem->InOwnPenaltyArea(Mem->OpponentAbsolutePosition(i)))
				attackers[attack++] =i;

	//find defenders
	for (int i=1; i<=Mem->SP_team_size; ++i)
		if (Mem->TeammatePositionValid(i) && i!=Mem->MyNumber)
			if (!TeammateMarking(i) && Mem->TeammateX(i)<-10)
				defenders[defend++] =i;

	//find dangerous
	if(fabs(Mem->BallY())>=	1/3*(Mem->SP_goal_area_length))
   		Sort(attackers,attack);
	else
   		SortAttackersByDistance(attackers, attack);

	FindAndSetDangerous(attackers,attack);

	//~ printf("\n TIME _________________%d\n",Mem->CurrentTime.t);
	//~ for (int i=0; i<attack; ++i)
        //~ printf("__Attack =%d_",attackers[i]);
			
	//~ for (int i=0; i<danger; ++i)
        //~ printf("_Danger =%d_",dangerous[i]);
	//~ printf("\n");
	
	//selection or no
	/*if (danger != 0) {			
     if (danger <= defend) {
       for (int i=0; i<danger; ++i){
		if((teammate_num=FindClosestTeammateToPoint(Mem->OpponentAbsolutePosition(dangerous[i]),defenders,defend)) != Unum_Unknown)    
		{
			MessagesMan.SayMarkMsg(teammate_num, dangerous[i]);	
			printf ("\n\n\n_____ defender =%d ",teammate_num);
		  printf("\n\n\n danger  =%d",dangerous[i]); 
		}		 
	  }
	  return;
   	}*/	

	//~ if(Mem->ClosestOpponentToBall()!=Unum_Unknown && Mem->OpponentPositionValid(Mem->ClosestOpponentToBall()))
    //~ {
		//~ if(fabs(Mem->BallY())>=
		//~ 1/3*(Mem->SP_goal_area_length))
	   		//~ Sort(dangerous,danger);
	//~ }
	//~ else
       //~ SortAttackersByDistance(dangerous, danger);
	
	int i=0;
	for (i=0;i<danger;i++)
	  if(dangerous[i]!=Unum_Unknown)
	    if ((teammate_num = FindClosestInTowRectangles(dangerous[i])))//Mem->OpponentInTeammateRect(&teammate_num,dangerous[i]) 
		  if(teammate_num != Unum_Unknown && Mem->TeammatePositionValid(teammate_num))	
			if(teammate_num != Mem -> MyNumber && !TeammateToldToMark(teammate_num,defenders_told_to_mark) && //
			(!TeammateMarking(teammate_num) || TeammateMarking(teammate_num,dangerous[i])))
			{
				if(!MessagesMan.SayMarkMsg(teammate_num, dangerous[i]))
				{
					//~ flag = i;	
					Mem->LogAction4(150,"PACKED MARK MESSAGE is not accepted Defnder %d _____Attacker %d ",
							teammate_num,dangerous[i]);
					return;
				}
				LastSayMarkTime = Mem->CurrentTime.t;
				//~ printf ("\n__________ defender =%d ",teammate_num);
				//~ printf("_____danger  =%d",dangerous[i]); 
				Mem->LogAction4(150,"DEFENDER : %d ________DEAGEROUS : %d",teammate_num,dangerous[i]);
				dangerous[i]=Unum_Unknown;
				defenders_told_to_mark[told_mark++]=teammate_num;
				marked++;			
				//~ temp_counter ++;
			}
			else
				if(teammate_num == Mem->MyNumber)	
					Mem->LogAction2(150,"GET MARK FUNCTION  DEFENDER == MYNUMBER");
		
	for(int i=0;i<danger;i++)
		if(dangerous[i]!=Unum_Unknown)  //this dangerous player hasn't been marked ....
		{
			
			// TODO THERE IS SOMEONE  THAT ARE NOT MARKED
		}
	if(marked != danger) //we still have unmarked opponents lets tell the closest ummarking teammate!
	{
		//~ printf("\nStrange!\n");
		int i=0;
		for( i=0;i<danger;i++)
			if(dangerous[i]!=Unum_Unknown)  //this dangerous player hasn't been marked ....
		    	if((teammate_num = FindClosestTeammateToPoint(Mem->OpponentAbsolutePosition(dangerous[i]),defenders,defend)) != Unum_Unknown 
				   && !TeammateToldToMark(teammate_num,defenders_told_to_mark) && teammate_num!=Mem->MyNumber)    
					{
						if(!MessagesMan.SayMarkMsg(teammate_num, dangerous[i]))
						{
							//~ flag=i;	
							Mem->LogAction4(150,"PACKED MARK MESSAGE is not accepted Defnder %d _____Attacker %d ",
							teammate_num,dangerous[i]);
							return;
						}
						LastSayMarkTime = Mem->CurrentTime.t;
						//~ printf ("\n_____ defender =%d ",teammate_num);
					  	//~ printf(" _______danger  =%d",dangerous[i]); 
						Mem->LogAction4(150,"DEFENDER : %d ________DEAGEROUS : %d",teammate_num,dangerous[i]);
						defenders_told_to_mark[told_mark++]=teammate_num;
						//~ counter++;
						//~ temp_counter ++;
					}
	}
				
	
    /*for (int i=0; i<defend; ++i)
    	if((teammate_num=FindClosestTeammateToPoint(Mem->OpponentAbsolutePosition(dangerous[i]),defenders,defend)) != Unum_Unknown)    
		{
			MessagesMan.SayMarkMsg(teammate_num, dangerous[i]);	
			printf ("\n\n\n_____ defender =%d ",teammate_num);
		  printf("\n\n\n danger  =%d",dangerous[i]); 
		}
	  return;	
	*/		
	//~ for(int i=counter+1;i<= Mem->SP_team_size;i++)
		//~ if(TeammateMarking(i))
				//~ if(temp_counter < 3)
				//~ {
					//~ MessagesMan.SayMarkMsg(teammate_num, dangerous[i]);	
					//~ printf ("\n_____ defender =%d ",teammate_num);
				  	//~ printf(" _______danger  =%d",dangerous[i]); 
					//~ defenders_told_to_mark[told_mark++]=teammate_num;
					//~ counter++;
					//~ temp_counter ++;
				//~ }	

			
	flag =0;	
}
//__________________________________________________________________________________
Unum PlayerGoalie::FindClosestTeammateToPoint(Vector point,int * Teammates,int num)
{
	float min_dist = 10,dist;
	Unum teammate = Unum_Unknown;
	int i , selected_i=0;
	for(i=0;i< num ;i++)
	{
		if(Teammates[i] != Unum_Unknown)
			if(Mem->TeammatePositionValid(Teammates[i]))
				if((dist = Mem->TeammateDistanceTo(Teammates[i],point)) < min_dist)
				{
					min_dist = dist;
					teammate = Teammates[i];
					selected_i = i;
				}
	}
	if(teammate != Unum_Unknown )
		Teammates[selected_i] = Unum_Unknown;
	return teammate;
}
//___________________________________________________________________________________________________________
Bool PlayerGoalie::OpponentIsDangerousThanOthers(Rectangle AdjacentRectangle,Unum opp){
	Unum opps[11];
	int oppIndex = -1;
	oppIndex = GetDangerIndex(opp);
	int index;
	for(int i=1;i<= Mem->SP_team_size;i++)
	{
		if(Mem->OpponentPositionValid(i))
			if(AdjacentRectangle.IsWithin(Mem->OpponentAbsolutePosition(i)))
					if((index = GetDangerIndex(opps[i])) >= 0)
						if(	index < oppIndex)
							return FALSE; // there is at least one opp in adjacentRectangle that is dangrous (its index is less than opp index)
	}
	return TRUE;
}
//___________________________________________________________________________________________________________
int PlayerGoalie::GetDangerIndex(Unum opp){
	int i;
	for(i=0;i<danger;i++)
		if(opp == dangerous[i])
			return i;
	return -1;
}
//___________________________________________________________________________________________________________
void PlayerGoalie::FindAndSetDangerous(Unum * attackers,int attack){
	int k=0,j;
	danger = 0;
	for(int i=0 ; i< attack;i++)
	{
		for(j = 1 ; j<= Mem->SP_team_size;j++)	
			if(TeammateMarking(j,attackers[i]) && j!= Mem->MyNumber)
			{	
				//~ MessagesMan.SayMarkMsg(j,attackers[i]);
				k++;
			}
		if(danger == k)
		{
			dangerous[danger++]=attackers[i];
			k=danger;
		}
	}
//~ printf("\n_____________danger:____%d",danger);	
}
//__________________________________________________________________________________
bool PlayerGoalie::TeammateToldToMark(Unum teammate,Unum teammates_told_to_mark[10])
{
	for(int i=0;i<=9;i++)
		if(teammate==teammates_told_to_mark[i])
			return TRUE;
	return FALSE;
}
//___________________________________________________________________________________________________________
Unum PlayerGoalie::FindClosestInTowRectangles(Unum opp, bool Inner_Call){
		Unum  RectOwner=Unum_Unknown,Adjacent=Unum_Unknown , YAdjacent=Unum_Unknown;
		Unum  YClosest = Unum_Unknown;
		Vector oppPos ;
		Mem->OpponentInTeammateRect(&RectOwner, opp);		
		if(!Inner_Call)
			Mem->OpponentInXAdjacentTeammateRect(&Adjacent,opp);	
		else
			Mem->OpponentInOuterYAdjacentTeammateRect(&Adjacent,opp);
		Mem->OpponentInOuterYAdjacentTeammateRect(&YAdjacent,opp);
		
		if(Mem->TeammateIsCornerPlayer(YAdjacent) && !Inner_Call){
			YClosest = FindClosestInTowRectangles(opp, TRUE);
		}
		//~ if(Adjacent == Unum_Unknown)
			//~ Mem->OpponentInYAdjacentTeammateRect(&YAdjacent,opp);
			if(Adjacent == Unum_Unknown)
				return ClosestTeammateToOpponent(YClosest,RectOwner,opp);
			if(RectOwner == Unum_Unknown)
				return ClosestTeammateToOpponent(YClosest,Adjacent,opp);
			if(Mem->OpponentPositionValid(opp))
				oppPos = Mem->OpponentAbsolutePosition(opp);
			else
				return RectOwner;
			if( RectOwner == Mem -> MyNumber )
				return ClosestTeammateToOpponent(YClosest,Adjacent,opp);;
			if(Adjacent == Mem->MyNumber)
				return ClosestTeammateToOpponent(YClosest,RectOwner,opp);
			if(TeammateMarking(RectOwner))
				if(TeammateMarking(Adjacent) )
					if(TeammateMarking(Adjacent,opp))
						return Adjacent;
					else
						return	YClosest;
				else
					if(TeammateMarking(RectOwner,opp))
						return RectOwner;
					else
						return ClosestTeammateToOpponent(YClosest,Adjacent,opp);
			if(TeammateMarking(Adjacent))
				if(TeammateMarking(Adjacent,opp))
						return Adjacent;
					else
						return ClosestTeammateToOpponent(YClosest,RectOwner,opp);
			if(!Mem->TeammatePositionValid(RectOwner))
				if(!Mem->TeammatePositionValid(Adjacent))
					return YClosest;
				else
					return ClosestTeammateToOpponent(YClosest,Adjacent,opp);
			else
				if(!Mem->TeammatePositionValid(Adjacent))
					return ClosestTeammateToOpponent(YClosest,RectOwner,opp);
				if(Mem->TeammateDistanceTo(RectOwner,oppPos) < Mem->TeammateDistanceTo(Adjacent,oppPos))
					return ClosestTeammateToOpponent(YClosest,RectOwner,opp);
				else
					if(OpponentIsDangerousThanOthers(Mem->TeammateHomeRectangle(RectOwner),opp))
						return ClosestTeammateToOpponent(YClosest,Adjacent,opp);
					else
						return ClosestTeammateToOpponent(YClosest,RectOwner,opp);
	
	}
//__________________________________________________________________________________
void PlayerGoalie::Sort(int A[],int len)
{
	for (int i=0; i<len-1; ++i)
  		for (int j=i+1; j<len; ++j)
			if(Mem->OpponentAbsolutePosition(A[i]).x>Mem->OpponentAbsolutePosition(A[j]).x-3)
   				if (Mem->OpponentAbsolutePosition(A[i]).x>Mem->OpponentAbsolutePosition(A[j]).x
					&& fabs(Mem-> BallY()) > Mem->SP_goal_width/2) 
   				{	
					int help =A[i];
		    		A[i] = A[j];
    				A[j] = help;
    			}		
				else
    			if ( Mem->OpponentAbsolutePosition(A[i]).x>Mem->OpponentAbsolutePosition(A[j]).x) {
	  				int help = A[i];
			  		A[i] = A[j];
	  				A[j] = help;
				}	
}
//___________________________________________________________________________________________________________
Unum PlayerGoalie::ClosestTeammateToOpponent(Unum first , Unum second, Unum Opponent){
	if(first == Unum_Unknown)
		return second;
	else
		if(second == Unum_Unknown)
			return first;
	Vector point = Mem->OpponentAbsolutePosition(Opponent);
	if(Mem->TeammateDistanceTo(first,point) > Mem->TeammateDistanceTo(second,point))	
		return second;
	else
		return first;
}
//________________________________________________________________________________________________________________________________________
bool PlayerGoalie::TeammateMarking(Unum teammate)
{
	//if teammate is marking somebody return True
	for(int i=1;i<= Mem->SP_team_size;i++)
			if(TeammateMarking(teammate,i))
				return TRUE;
	return FALSE;
}

//____________________________________________________________________________________________________________
bool PlayerGoalie::TeammateMarking(Unum teammate,Unum opp)
{
	if(!Mem->TeammatePositionValid(teammate) || !Mem->OpponentPositionValid(opp))
	{
		//~ printf("Teammate or Opponent Position not valid PlayerGoalie.C1425\n");
		return FALSE;
	}
	//returns True if teammate is marking opp
	//~ if(IsPointInCone(Mem->TeammateAbsolutePosition(teammate),MARK_RATIO,Mem->BallAbsolutePosition(),
			//~ Mem->OpponentAbsolutePosition(opp)))
		if(Mem->TeammateDistanceTo(teammate,Mem->OpponentAbsolutePosition(opp))<= MARK_DIST)
			return TRUE;
	return FALSE;		
}

//__________________________________________________________________________________
void PlayerGoalie::SortAttackersByDistance(int A[],int len)
{
  Line GoalLine;
  GoalLine.LineFromTwoPoints(Mem->MarkerPosition(gfb), Mem->MarkerPosition(gft));	
  for (int i=0; i<len-1; ++i)
	  for (int j=i+1; j<len; ++j)
		  if (Mem->OpponentDistanceToLine(A[i], GoalLine) > Mem->OpponentDistanceToLine(A[j], GoalLine) ) {
			  int help = A[i];
			  A[i] = A[j];
			  A[j] = help;
		  }	  
 return;
 }
//__________________________________________________________________________________
Vector PlayerGoalie::BestPosition()
{
	Line l;
	Vector center;
	if(Mem->MySide=='l')
	  center = Mem->MarkerPosition (Flag_L0);
	else
	  center = Mem->MarkerPosition (Flag_R0);
	Vector Intersect1, Intersect2, Intersect;
	float rad = 7;
	Vector goal_poin = Mem->MarkerPosition (Mem->RM_My_Goal);
	if(Mem->BallY()> 0)
		goal_poin.y = Mem->BallY() / 10;
	else
		goal_poin.y = Mem->BallY() / 10;
	l.LineFromTwoPoints (goal_poin,
			     Mem->BallAbsolutePosition ());
	LineCircleIntersect (l, rad, center, &Intersect1, &Intersect2);
	Intersect =	( (Intersect1.x > - Mem->SP_pitch_length/2) ? Intersect1 : Intersect2);
	//~ ((Mem->BallAbsolutePosition ()).dist (Intersect1) <
		 //~ (Mem->BallAbsolutePosition ()).
		 //~ dist (Intersect2) ? 
	Vector point = Vector(-50.5,l.get_y(-50.5));
	//~ if(OneToOneFlag)
	//~ {
			//~ point.x = -47.5;
			//~ point.y = l.get_y(point.x);
	//~ }
		
	if(point.x < Mem->MyX() && Mem-> BallX() < -(Mem->SP_pitch_length/2 -( Mem->SP_goal_area_length +
					(Mem->SP_penalty_area_length - Mem->SP_goal_area_length)/2 ))) //InOwnPenaltyArea())
	{
			point.x = Mem-> MyX();
			point.y = l.get_y(point.x);
	}
	
	if((point.y) >(Mem->SP_goal_width/2)-1)
	{
		point.y = (Mem->SP_goal_width/2)-.6;
		point.x = -51;
	}
	else
		if((point.y) < -(Mem->SP_goal_width/2)+1)
		{
			point.y = -(Mem->SP_goal_width/2)+.6;
			point.x = -51;
		}
	return point;
}
//__________________________________________________________________________________
bool PlayerGoalie::CanGetBall(){
	Line ball_line;
	bool opponent_seen = FALSE;
	Vector ball_path_intercept_point;
	float intercept_point_dist_to_ball;
	float opp_dist_to_line,opp_dist_to_ball;
	float my_distance_to_intercept_point;
	float result;
	if(!Mem->BallPathInterceptCanIGetThere())
		return FALSE;
	ball_path_intercept_point = Mem->BallPathInterceptPoint();
	intercept_point_dist_to_ball = ball_path_intercept_point.dist(Mem->BallAbsolutePosition());
	my_distance_to_intercept_point = Mem->DistanceTo(ball_path_intercept_point);
	ball_line = Line(Ray(Mem->BallAbsolutePosition(),Mem->BallAbsoluteVelocity()));
	for(int i=1 ; i<= Mem->SP_team_size; i++)
		if(Mem->OpponentPositionValid(i)){
			bool opponent_seen = TRUE;
			opp_dist_to_ball = Mem -> OpponentDistanceToBall(i);
			opp_dist_to_line = ball_line.dist(Mem->OpponentAbsolutePosition(i));
			result = opp_dist_to_ball*opp_dist_to_ball - opp_dist_to_line*opp_dist_to_line;
			result = sqrt(result);
			if(result < intercept_point_dist_to_ball)
				if(my_distance_to_intercept_point > opp_dist_to_line)
					return FALSE;
			}
	return opponent_seen;		
}
//__________________________________________________________________________________
bool PlayerGoalie::BallGoToGoal(){
Vector intersect;
	Ray RBallPath(Mem->BallAbsolutePosition(),Mem->BallAbsoluteVelocity());
	Line BallVelLine = Line(RBallPath);
	intersect = BallVelLine.intersection(GoalieLine);
	return (Mem->InOwnGoalArea(intersect) && (fabs(intersect.y) < Mem->SP_goal_width/2+.3));
	}
//__________________________________________________________________________________
int PlayerGoalie::shooted ()
{
	Unum opp , pn;
	float dist=0,ball_speed = Mem->BallSpeed ();	
	Vector ball_pos = Mem->BallAbsolutePosition();
	if((opp=Mem->ClosestOpponentToBall()) != Unum_Unknown)
		if(Mem->OpponentPositionValid(opp))
			//~ dist = Mem-> OpponentDistanceToBall(opp);
		if(Mem-> BallSpeed() > .8 && Mem->OpponentDistanceToBall(opp) < Mem->SP_kickable_area)
			kicker = opp;
		if(BallGoToGoal())
			{
				//~ if(Mem->BallPathInterceptCanIGetThere())
				//~ {
				if(!Mem->OpponentVelocityValid(kicker))
					return shoot_kick;
				if(fabs(Mem->OpponentSpeed(kicker)- ball_speed) > .62 
					|| Mem->OpponentDistanceToBall(kicker) > Mem-> SP_kickable_area + 1.5)
					return shoot_kick;
				else
					if(Mem->OpponentDistanceToBall(kicker) <= Mem-> SP_kickable_area +1.5
						&& fabs(Mem->OpponentSpeed(kicker)- ball_speed) < .62 )
						return no_kick;
					else
						return maybe_shoot_kick;
				//~ }
				//~ else
					//~ return no_kick;
		}
			else{
				Unum pn;
				if( (pn = FindTarget()) > 0 )//Mem->BallPathInterceptCanIGetThere() &&
					if(Mem->OpponentPositionValid(pn))
					{//~ Line ballLine = Line(Ray(Mem->BallAbsolutePosition(),Mem->BallAbsoluteVelocity()));
					//~ for(int i=1;i<Mem->SP_team_size;i++)
							//~ if(Mem->OpponentPositionValid(i))
								//~ if(ball_pos.dist(Mem->BallPathInterceptPoint()) < ball_pos.dist(Mem->OpponentAbsolutePosition(i) + 2) )	 
								//~ if(ballLine.dist(Mem->OpponentAbsolutePosition()) < min)
					if((fabs(Mem->OpponentSpeed(kicker)- ball_speed) > .4 
					|| Mem->OpponentDistanceToBall(kicker) > Mem-> SP_kickable_area + 1.5) ||
						Mem->OpponentDistanceToBall(kicker) <= Mem-> SP_kickable_area +1.5
							&& fabs(Mem->OpponentSpeed(kicker)- ball_speed) < .62 )
								if(CanGetBall())//ball_pos.dist(Mem->BallPathInterceptPoint()) < ball_pos.dist(Mem->OpponentAbsolutePosition(pn) + 2) )	 
									// TODO : WHICH CAN GET THE BALL FASTER									
									//~ if(Mem->OpponentDistanceTo(pn,Mem->BallPathInterceptPoint()) > 
							//~ Mem-> DistanceTo(Mem->BallPathInterceptPoint())+1){
								return my_pass_kick;
							
						else
							return pass_kick;
					}
					else
						return my_pass_kick;
			}
	return no_kick;
}

//__________________________________________________________________________________

void
PlayerGoalie::GetShoot ()
{
	Mem->LogAction2(150,"________Get SHOOT");
	static int i = 0;
	OneToOneFlag = FALSE;
	Vector point;
	//~ printf("\n_SHOOOOOOOOOOOOOT  _____ SHOOT\n");
	if (Mem->BallInOwnPenaltyArea ())
	{
		if( Mem->BallPathInterceptCanIGetThere())
			{
			point = Mem->BallPathInterceptPoint ();
			//~ Line line;
			//~ line.LineFromTwoPoints(Mem->BallAbsolutePosition(),point);			
			//~ point.x -= 2;
			//~ point.y = line.get_y(point.x);			//~ HLAction.goalie_go_to_point(point);
			}
		else
			{
			Line ballVellLine(Ray(Mem->BallAbsolutePosition(),Mem->BallAbsoluteVelocity()));
			point.x = -50;
			point.y = ballVellLine.get_y(point.x);		
		}
		HLAction.goalie_go_to_point (point, 1, 100);
		
	}
	else
		GoToNimsazAndLinePrj();
	//~ if(Mem-> BallX() <= Mem -> MyX() || Mem-> OpponentDistanceToBall(Mem->ClosestOpponentToBall()) < Mem->SP_kickable_area 
		//~ || Mem-> TeammateDistanceToBall(Mem->ClosestTeammateToBall()) < Mem->SP_kickable_area)
		//~ shootcontinue = FALSE;
}

//____________________________________________________________________/////////////////////////////////////*/
void PlayerGoalie::GetOpponentPass(){
	Mem->LogAction2(150,"______Get OPPONENTPASS");
	OneToOneFlag = FALSE;
	Vector point = Mem->BallPathInterceptPoint();
	Line line;
			line.LineFromTwoPoints(Mem->BallAbsolutePosition(),point);			
			point.x -= 2;
			point.y = line.get_y(point.x);			
	//~ printf("\n _GET PASSSSSSSSSSSSSSSSSSSSSSS\n");
	if(Mem->BallInOwnPenaltyArea())
		if( CanGetBall())//Mem->BallPathInterceptCanIGetThere() )
			HLAction.goalie_go_to_point(point);
		else
			GoToNimsazAndLinePrj();
	else
		GoToNimsazAndLinePrj();
}
//____________________________________________________________________/////////////////////////////////////*/
void
PlayerGoalie::DontKnowMyPosition ()
{
	HLAction.scan_field_with_body ();
}

//____________________________________________________________________//////////////////////////////////////*/
void
PlayerGoalie::DontKnowBallPosition ()
{
	//~ dont_know_ball_pos_cycles++;
	//~ HLAction.face_only_neck_to_ball();
	//~ printf("\n___DONT KNOW BALL POSITION\n");
	HLAction.scan_field_with_body ();
}

//__________________________________ added by H & KH __________________________________/////////////////////*/

bool PlayerGoalie::Ifcare ()
{
	return ((Mem->BallAbsolutePosition ().x <
		 (40 - (Mem->SP_pitch_length) / 2.0f))
		|| Mem->BallDistance () < 30);
}

//__________________________________/ added by H & KH __________________________________/////////////////////*/

void
PlayerGoalie::Dawdle ()
{
	//Vector GuardPoint(10.0f - (Mem->SP_pitch_length)/2.0f ,0);
	//~ Vector GuardPoint( ((Mem->MarkerPosition(top)).x+3) ,0);
	//~ HLAction.goalie_go_to_point(GuardPoint);
	//~ if(Mem-> BallAngleFromBody() > 10 )
	//~ HLAction.face_neck_and_body_to_ball();
	//~ else
//~ if(Mem-> BallAngleFromBody() > 5)   
	//~ HLAction.face_only_body_to_ball();
//~ else
	GoToNimsazAndLinePrj ();
}

//__________________________________ added by H & KH __________________________________/////////////////////*/
void
PlayerGoalie::getOneToOne ()
{
	//~ printf("\nGET ONE TO ONE  GET ONE TO ONE \n");
	Mem->LogAction2(150,"____GET OneToOne");
	Rectangle rec;
	Line line;
	line.LineFromTwoPoints (BestPosition(),	Mem->BallAbsolutePosition ());
	Vector point3 = Vector((BestPosition().x + Mem->BallX())/2,line.get_y((BestPosition().x + Mem->BallX())/2));
	Vector point = Vector (Mem->MyX()+.4,    line.get_y (Mem->MyX() ));
	Vector ballPoint = Mem->BallPathInterceptPoint();
	Line l ;
	Vector goal_point;
	Unum opp;
	goal_point.y = Mem->BallY() / 19;
	l.LineFromTwoPoints (goal_point,Mem->BallAbsolutePosition ());
	//~ ballPoint.x-=1;
	ballPoint.y = l.get_y(ballPoint.x);
	if (Mem->BallInOwnPenaltyArea ())
	{
		if(Mem->BallDistance() > 3)
		{	if((opp=Mem->ClosestOpponentToBall()) != Unum_Unknown)
				if(Mem->OpponentPositionValid(opp))
					//~ if(Mem->OpponentDistanceToBall(opp) > 3)
					{
						HLAction.goalie_go_to_point(Mem->BallAbsolutePosition());
						return;
					}
					//~ else
					//~ {
						//~ int factor = 1;
						//~ if(Mem->MyBodyAng() < 0)	
							//~ factor = -1;
						//~ turn(factor*(90-fabs(Mem->MyBodyAng())));
						//~ return;
					//~ }
						HLAction.goalie_go_to_point(Mem->BallPathInterceptPoint());
		}
		else
	if( CanGetBall())//Mem-> BallPathInterceptCanIGetThere())
		HLAction.goalie_go_to_point(Mem->BallPathInterceptPoint());
	else
		HLAction.goalie_go_to_point(Mem->BallAbsolutePosition());
	}
	else	
	{
		//~ printf ("\n  _________6________\n");
		point.x = -40;
		point.y = line.get_y (point.x);
		Vector point1 = BestPosition();
		Vector point2 = Vector((point1.x + Mem->BallX() )/ 2,(point1.y + Mem->BallY() )/ 2);
		HLAction.goalie_go_to_point(point1);
	}	
}
/*/////////////////////////////////////// added by H & KH __________________________________////////*/

void
PlayerGoalie::TreatWithBall ()
{

	int DistanceSafeHold = 2;
	if (CycleCatchedBall == 50)
	{
		if ((Mem->
		     TeammateDistanceTo (Mem->MyNumber,
					 Mem->ClosestOpponentTo (Mem->
								 MyNumber))) <
		    DistanceSafeHold)
			HLAction.
				kick_ball (Vector
					   (0,
					    (Mem->MarkerPosition (Flag_TR40).
					     y)), KM_Quickly, TURN_AVOID);

		else
			//can pass
			HLAction.pass_ball (Mem->
					    ClosestTeammateTo (Mem->MyNumber,
							       FALSE), 1);

	}
	else
	{			//yet can hold ball
		++CycleCatchedBall;
		//~ //printf ("\n\n\n***CycleCatchedBall = %d",CycleCatchedBall);
	}
}

/*/ __________________________________ ____________________________________________________________________///////*/
void PlayerGoalie::Goal_Kick_Pass(){
		OneToOneFlag = FALSE;
	int DistanceSafeHold = 2;
	Unum num,opponent;
	//~ if (CycleCatchedBall == 50)
	//~ {
	if(Mem->BallInOwnPenaltyArea() )
	{
		if(!Mem-> BallKickable())
		HLAction.get_ball();	
	else{
		if((opponent = Mem->ClosestOpponent()) != Unum_Unknown)
			//~ if(Mem->OpponentDistance(opponent) <  DistanceSafeHold)
				//~ HLAction.kick_ball (FindFreePointForShoot(), KM_Quickly, TURN_AVOID);
			//~ else
			for(int i=1;i<Mem->SP_team_size;i++)
				if(Mem->TeammatePositionValid(i) && Mem->MyNumber != i)
					ActionListMan.AddToActionList(
						new ActionPassActive(AS_Myself,AR_NoReason, 1,i,&HLAction, &History,&SayAMessage),"Goal_Kick_Pass");
		//~ if((num =FindTeammatesFront()) != Unum_Unknown)//can pass
				//~ {
					//~ Vector point = Mem->TeammateAbsolutePosition(num);
					//~ point.x -= 5;
					//~ HLAction.kick_ball(point,KM_Moderate,2,TURN_CLOSEST );
					//~ Mem->LogAction2(150,"____GOAL KICK PASSS ________________ PASS");
					//~ if(!MessagesMan.SayPassMsg(num,point)){
						//~ MessagesMan.Clear();
						//~ MessagesMan.SayPassMsg(num,point);
						//~ WantToSayPass = TRUE;
						//~ PassReceiver = num;
						//~ }
				//~ }
				else
					HLAction.kick_ball(FindFreePointForShoot(),KM_Moderate, TURN_CLOSEST);
		else
				for(int i=1;i<Mem->SP_team_size;i++)
				if(Mem->TeammatePositionValid(i) && Mem->MyNumber != i)
					ActionListMan.AddToActionList(
						new ActionPassActive(AS_Myself,AR_NoReason, 1,i,&HLAction, &History,&SayAMessage),"Goal_Kick_Pass");
		//~ if((num =FindTeammatesFront()) != Unum_Unknown)//can pass
				//~ {
					//~ Vector point = Mem->TeammateAbsolutePosition(num);
					//~ point.x -= 5;
					//~ HLAction.kick_ball(point,KM_Moderate,2,TURN_CLOSEST );
					//~ Mem->LogAction2(150,"____GOAL KICK PASSS ________________ PASS");
					//~ if(!MessagesMan.SayPassMsg(num,point)){
						//~ MessagesMan.Clear();
						//~ MessagesMan.SayPassMsg(num,point);
						//~ WantToSayPass = TRUE;
						//~ PassReceiver = num;
						//~ }
				//~ }
				else
					HLAction.kick_ball(FindFreePointForShoot(),KM_Moderate, TURN_CLOSEST);
		I_Kicked = TRUE;
		}
	}
//~ }
else
	GoToNimsazAndLinePrj();
}
/*/ __________________________________ ____________________________________________________________________///////*/
Vector PlayerGoalie::FindFreePointForShoot(){
	Line line,vert_line;
	Vector point;
	bool MustContinue= FALSE;
	Unum opponents_in_my_half[11];
	int j=0,k=0;
	for(int i = 1 ; i< Mem->NumOpponents();i++)
		if(Mem->OpponentPositionValid(i) && Mem->OpponentX(i)<0)
			opponents_in_my_half[j++] = i;
		
	vert_line.LineFromTwoPoints(Vector(-10,-Mem->SP_pitch_width/2),Vector(-10,Mem->SP_pitch_width/2));
	point.x = -10;
		int i;
	for(i= (int)- Mem -> SP_pitch_width/2 ;i< Mem->SP_pitch_width/2;i+=4);
	{
		MustContinue = FALSE;
		point.y = (float)i;	
		line.LineFromTwoPoints(Mem->MyPos(),point);	
		for( k=0;k< j;k++)
			if(Mem->OpponentDistanceToLine(opponents_in_my_half[k],line)<2)
			{
				MustContinue = TRUE;
				break;
			}
		if(!MustContinue)
			return point;	
	}
	}
/*/ __________________________________ ____________________________________________________________________///////*/

float
PlayerGoalie::GetCatchAngle ()
{
	/* Vector CatchVector = Mem->BallAbsolutePosition() - Mem->MyPos();
	 * //printf ("\n\n\n***Catch angle = %f",CatchVector.dir());    
	 * float A= CatchVector.dir();
	 * AngleDeg B= Mem->get_abs_ball_ang();
	 * return A - B;
	 * }   */
	AngleDeg angle = Mem->BallAngleFromBody ();
	//~ angle = angle*(1.0 + Mem->SP_inertia_moment*Mem->MySpeed());
	return angle;
	//~ return (angle>0) ? angle + 5 : angle - 5 ;
}
//__________________________________________________________________________________//////
Unum PlayerGoalie::FindTeammatesFront()
{
	int num_opps = 11,num;
	Unum choice= Unum_Unknown;
	for(int i=1;i<Mem->SP_team_size;i++){
		if(Mem->MyNumber == i)
			continue;
		if(Mem->TeammatePositionValid(i))
			if(Mem->TeammateIsCornerPlayer(i))
			//~ if((num=Mem->NumOpponentsWithin(5) ) <= num_opps){
				//~ num_opps = num;
			 choice = (Unum)i;
			//~ }
		}
	return choice;
}
//__________________________________________________________________________________//////
Unum PlayerGoalie::ChooseFromCandidates(Unum * Candidates, int num_candidates)
{
	bool changed = FALSE;
   	Line l;
    Unum Interceptors[11] = {0}, choice = Unum_Unknown;			// =Candidates[0];
    int num_interceptors = 0, j = 0;
    float min_dist= Mem->SP_pitch_length * 2 ,min_distance = Mem->SP_pitch_length * 2, max_min_distance = 0;
    int i = 0;
    Vector point;
    float myX = Mem->MyX() ;
	float teammateX ;
	if (num_candidates == 0)
		return 0;
    for (i = 0; i < num_candidates; i++)
	{
		changed = FALSE;
		l.LineFromTwoPoints(Mem->MyPos(),Mem->TeammateAbsolutePosition(Candidates[i]));
		min_dist= Mem->SP_pitch_length * 2;
		teammateX = Mem->TeammateX(Candidates[i]) ;
		for(int j=1 ;j<= Mem->SP_team_size;j++)
		{
			if(Mem -> OpponentPositionValid(j) )
				if( Mem->TeammateDistanceTo(Candidates[i],Mem->OpponentAbsolutePosition(j)) > 2){
				//~ if(Mem->OpponentX(j)>= myX && Mem->OpponentX(j)<= teammateX)
					{
						//~ changed	= TRUE;
						if(l.dist(Mem->OpponentAbsolutePosition(j)) < min_dist)
							min_dist = l.dist(Mem->OpponentAbsolutePosition(j));
					}
				}
				else
					changed = TRUE;
		}
		if(changed)
			continue;
			//~ min_dist = Mem->SP_pitch_length;
		if(min_distance < min_dist)
		{
			min_distance = min_dist;
			choice = Candidates[i];
		}
    }
    if (min_distance < Mem->SP_kickable_area)
		return Unum_Unknown;
    return choice;
	//~ return Candidates[num_candidates-1];
}
//__________________________________________________________________________________//////
int PlayerGoalie::NumOpponentInPenaltyArea(){
	int j=0;
	for(int i=0;i< Mem->SP_team_size;i++)
		if(Mem->OpponentPositionValid(i))
			if(Mem->InOwnPenaltyArea(Mem->OpponentAbsolutePosition(i)))
				j++;	
			return j;
	}
//__________________________________________________________________________________//////
Vector PlayerGoalie::FurtherMyGoalFlagfromBall ()
{
	float
		dist1,
		dist2;
	dist1 = Mem->BallAbsolutePosition ().dist (Mem->
						   MarkerPosition (Mem->
								   RM_My_Goal_T));
	dist2 = Mem->BallAbsolutePosition ().dist (Mem->
						   MarkerPosition (Mem->
								   RM_My_Goal_B));
	if (dist1 > dist2)
		return (Mem->MarkerPosition (Mem->RM_My_Goal_T));
	else
		return (Mem->MarkerPosition (Mem->RM_My_Goal_B));
}
// ____________________________________________________________________///////////////////////////////Added 
// by Group1
Vector PlayerGoalie::NearestMyGoalFlagfromBall ()
{
	float
		dist1,
		dist2;
	dist1 = Mem->BallAbsolutePosition ().dist (Mem->
						   MarkerPosition (Mem->
								   RM_My_Goal_T));
	dist2 = Mem->BallAbsolutePosition ().dist (Mem->
						   MarkerPosition (Mem->
								   RM_My_Goal_B));
	if (dist1 < dist2)
		return (Mem->MarkerPosition (Mem->RM_My_Goal_T));
	else
		return (Mem->MarkerPosition (Mem->RM_My_Goal_B));
}

// ____________________________________________________________________///////////////////////////////Added 
// by Group1
void
PlayerGoalie::GoToConeNimsaz ()
{
	//~ printf("\n_GOTO CONE NIMSAZ _________\n");
	static int counter = 0, fac = 1;
	Vector F_Flag = FurtherMyGoalFlagfromBall ();
	Vector N_Flag = NearestMyGoalFlagfromBall ();
	Vector F_FlagRelPos =
		F_Flag.Global2Relative (Mem->BallAbsolutePosition (), 0.0);
	Vector N_FlagRelPos =
		N_Flag.Global2Relative (Mem->BallAbsolutePosition (), 0.0);
	Vector End =
		(F_FlagRelPos +
		 N_FlagRelPos * (F_FlagRelPos.mod () / N_FlagRelPos.mod ())) /
		2;
	End = End.Relative2Global (Mem->BallAbsolutePosition (), 0.0);
	Line Nimsaz;
	Nimsaz.LineFromTwoPoints (Mem->BallAbsolutePosition (), Mem-> MarkerPosition(Mem->RM_My_Goal));
	
	Vector prj = Nimsaz.ProjectPoint (Mem->MyPos ());
	float Yrnd = counter * .1 * fac;
	fac *= -1;
	prj.y += Yrnd;
	counter++;
	if (counter > 6)
		counter = 0;
	HLAction.goalie_go_to_point (prj, .6, 100);
}

//__________________________________________________________________////////////////////////////// */
void
PlayerGoalie::goalieMovingtoLine ()
{

	Vector F = Mem->MarkerPosition (bottom);
	Vector E = Mem->MarkerPosition (top);
	Vector C = Mem->MarkerPosition (gfb);
	Vector D = Mem->MarkerPosition (gft);

	//~ printf ("\n***Go to line");
	//printf ("\n\n\n***At first Ball Velocity X=%f",Mem->BallAbsoluteVelocity().x);       
	if (Mem->BallAbsoluteVelocity ().x < 0)
	{
		float x1 = Mem->BallAbsoluteVelocity ().x;
		float y1 = Mem->BallAbsoluteVelocity ().y;
		float x2 = Mem->BallAbsolutePosition ().x;
		float y2 = Mem->BallAbsolutePosition ().y;
		float A = (y2 - y1) / (x2 - x1);
		float B = -1;
		float G = ((y1 - y2) / (x2 - x1)) * x1 + y1;
		Line Ballline (A, B, G);
		if (BallPathBeetwinGoal (GoalieLine, Ballline, C, D))
		{

			// printf ("\n\n***wait = %d",Wait);   
			switch (BallState ())
			{
			case shoot:
				//~ printf ("\n\n\n***Shoot"); 
				shootcontinue = TRUE;
				GetShoot ();
				break;
			case pass:
				//~ printf ("\n\n\n***Pass");
				GoToConeNimsaz ();
				shootcontinue = FALSE;
				break;
			case notfree:
				//~ printf ("\n\n\n***notfree");
				if (Mem->BallAbsolutePosition ().x < F.x)
					HLAction.my_goalie_go_to_point (Mem->
								     BallAbsolutePosition
								     (), 1,
								     100);
				else
					GoToConeNimsaz ();
				shootcontinue = FALSE;
				break;
			case uncomplete:
				//~ //printf ("\n\n\n***uncomplete");
				GoToConeNimsaz ();
				shootcontinue = FALSE;
				break;
			default:
				//~ printf ("\n\n\n***defualt"); 
				shootcontinue = TRUE;
				GetShoot ();
				break;
			}	//switch
		}
		else
		{		//ball not goal
			if ((D.y < Mem->BallAbsolutePosition ().y)
			    && (Mem->BallAbsolutePosition ().y < C.y))
			{
				//~ printf ("\n\n\n***Ball not to goal");
				HLAction.my_goalie_go_to_point (Vector
							    ((E.x + 2),
							     Mem->
							     BallAbsolutePosition
							     ().y), 1, 100);
			}
			else if (Mem->BallAbsolutePosition ().y > 0)
			{
				//~ printf ("\n\n\n***Ball not to goal & BallVel not to goal");
				HLAction.
					goalie_go_to_point (Vector
							    (E.x + 2,
							     (C.y) / 2), 1,
							    100);
			}
			else
				HLAction.
					goalie_go_to_point (Vector
							    (E.x + 2,
							     (D.y) / 2), 1,
							    100);
		}

	}


}				//function


//________________________________//////////// added by H & KH __________________________________//
	bool PlayerGoalie::BallPathBeetwinGoal (Line GoalieLine,
						Line Ballline, Vector C,
						Vector D)
{

	if ((D.y <= (GoalieLine.intersection (Ballline)).y)
	    && ((GoalieLine.intersection (Ballline)).y <= C.y))
		return (TRUE);
	else
		return (FALSE);
}

//________________________________///added by H & KH __________________________________///

int
PlayerGoalie::BallState ()
{
	//~ while (Wait <= wait) {
	//~ if ( CompareVelocity() ) {
	//~ ++Wait;
	//~ return uncomplete;
	//~ }//if
	//~ else {
	//~ Wait = 0;
	//~ return notfree;
	//~ }//else
	//~ }//while

	switch (FindTarget ())
	{
	case 0:
		Wait = 0;
		//~ //printf("\n______________________\n");
		return uncomplete;
		break;
	case -1:
		Wait = 0;
		return shoot;
		break;
	case 2:
	case 3:
	case 4:
	case 5:
	case 6:
	case 7:
	case 8:
	case 9:
	case 10:
	case 11:
		Wait = 0;
		return pass;
	default:
		Wait = 0;
		return shoot;
	}
}

//________________________________//added by H & KH __________________________________/////

bool PlayerGoalie::CompareVelocity ()
{
	static float
		initvel;
	float
		direction =
		(Mem->BallAbsoluteVelocity ().y) /
		(Mem->BallAbsoluteVelocity ().x);
	float
		norm = (Mem->BallAbsoluteVelocity ()).
		mod ();
	//printf ("\n\n\nnorm =%f ",norm );

	if (Wait == 0)
	{
		initvel = norm;
		BallPostNorm = norm;
		BallPostDir = direction;
		return TRUE;
	}

	if ((BallPostNorm > norm)
	    && ((direction >= BallPostDir - 0.1)
		&& (direction <= BallPostDir + 0.1))
	    && initvel > Initshootvelocity)
	{
		BallPostDir = direction;
		BallPostNorm = norm;
		return TRUE;
	}
	else
		return FALSE;
}

//////////////////////////////////// Added by H & KH \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
Unum PlayerGoalie::FindTarget ()
{
	bool		NoOneValid =TRUE;
	float		MinDistance;
	float		x1 =Mem->BallAbsoluteVelocity ().x;
	float		y1 =Mem->BallAbsoluteVelocity ().y;
	float		x2 =	Mem->	BallAbsolutePosition ().x;
	float		y2 =	Mem->	BallAbsolutePosition ().y;
	float		A = -(y1 / x1);
	float		B =		1;
	float		C = (y1 / x1) *	x2 -y2;
	Line	BallVelLine (A, B, C);
	int		Target2[10],j =	0;
	float		Min;
	Unum		Target;
	for (int i = 2; i <= Mem->SP_team_size; ++i)

		if (Mem->OpponentPositionValid (i))
		{
			NoOneValid = FALSE;
			if (((Mem->OpponentDistance2ToLine (i, BallVelLine)) <
			     Mem->SP_kickable_area)
			    )
			//~ && (Mem->OpponentAbsolutePosition (i).x <
				//~ Mem->BallAbsolutePosition ().x))
			{
				Target2[j] = i;
				++j;
			}
		}
	if (NoOneValid)
	{
		HLAction.scan_field_with_body ();
		return 0;
	}
		int num=0;	
	if (j == 0)
		return (-1);	// did not find next target in ballvelline
	else
	{

		float seccondmindistance =
				MinDistance =
					Mem->OpponentDistanceTo (Target2[0],
							 Mem->
							 BallAbsolutePosition ());
		int
			i;
		for (i = 0; i < j; ++i)
			if ((MinDistance) >
			    (Min =
			     Mem->OpponentDistanceTo (Target2[i],
						      Mem->
						      BallAbsolutePosition
						      ()))) ;
		{
			seccondmindistance = MinDistance;
			num = Target;
			MinDistance = Min;
			Target = Target2[i];
		}
	}
	//~ printf("\n\n\n***Target =%d",Target);     
	return (num);

}

//________________________________________________________________________________________///

bool PlayerGoalie::OneToOne ()
{
	
	Unum teammate,opponent;
	if(Mem-> BallInOwnPenaltyArea() )
	if((opponent =Mem->ClosestOpponentToBall()) != Unum_Unknown && Mem->OpponentPositionValid(opponent)){
		if((teammate = Mem->ClosestTeammateTo(
			Mem->OpponentAbsolutePosition(opponent),FALSE)) == Unum_Unknown )
			return TRUE;
		if(!Mem->TeammatePositionValid(teammate))
			return TRUE;
		if(//(Mem->TeammateX(teammate) > Mem->OpponentX(opponent)+ 3) ||
			((Mem->TeammateDistanceTo(teammate,Mem->OpponentAbsolutePosition(opponent)) > 5 
				&& Mem->OpponentDistanceTo(opponent,Mem->MarkerPosition(Mem->RM_My_Goal)) 
		< Mem->SP_penalty_area_length- 2 )|| (Mem-> BallDistance() < 3 &&  CanGetBall())//Mem->BallPathInterceptCanIGetThere())
		&& Mem->OpponentDistanceToBall(opponent)>4))
					if(fabs(Mem->BallY()) < (Mem->SP_goal_width/2 + 2))
						return TRUE;
					else
						return FALSE;
	}
	else
		return TRUE;
	//~ if ((!AnyOneWithoutMark() && Mem-> BallPossessor() < 0)
		 //~ || Mem->NumTeammatesWithin(Mem->SP_penalty_area_length,Mem->MarkerPosition(Mem->RM_My_Goal)) <= 0  || numOpponentInPenaltyArea() <= 1)
			//~ if (Mem->BallInOwnPenaltyArea())
			//~ if (((Mem->BallAbsolutePosition ().y >=   Mem->MarkerPosition (Flag_RT10).y)
			     //~ && (Mem->BallAbsolutePosition ().y <=
				 //~ Mem->MarkerPosition (Flag_RB10).y)) )
				//~ return TRUE;
	OneToOneFlag = FALSE;
	return FALSE;
	
	
	//~ Unum teammate,opponent;
	//~ if((opponent =Mem->ClosestOpponentToBall()) != Unum_Unknown ){
		//~ if((teammate = Mem->ClosestTeammateTo(
			//~ Mem->OpponentAbsolutePosition(opponent),FALSE)) == Unum_Unknown )
			//~ return TRUE;
		//~ if(Mem->TeammateX(teammate) < Mem->OpponentX(opponent)+ 1)
			//~ return TRUE;
		//~ if((Mem->TeammateDistanceTo(teammate,Mem->OpponentAbsolutePosition(opponent)) > 3 
			//~ && Mem->OpponentDistanceTo(opponent,Mem->MarkerPosition(Mem->RM_My_Goal)) 
		//~ < Mem->SP_penalty_area_length- 1 && BallGoToGoal())|| (Mem-> BallDistance() < 3 && Mem->BallPathInterceptCanIGetThere()))
			//~ return TRUE;
	//~ }
	//~ else
		//~ if(Mem-> BallInOwnPenaltyArea())
		//~ return TRUE;
	//~ return FALSE;
}

/////__________________________________  __________________________________/
int PlayerGoalie::numTeammateInPenaltyArea(){
	int count =0;
	for(int i = 2 ;i< Mem->NumTeammates()+1;i++){
		if(Mem-> TeammatePositionValid(i) )
			if(Mem->InOwnPenaltyArea(Mem->TeammateAbsolutePosition(i)))
				count++;
		}
	return count;
	}
/////__________________________________  __________________________________/
int PlayerGoalie::numOpponentInPenaltyArea(){
	int count =0;
	for(int i = 1 ;i< Mem->NumTeammates()+1;i++){
		if(Mem-> OpponentPositionValid(i) )
			if(Mem->InOwnPenaltyArea(Mem->OpponentAbsolutePosition(i)))
				count++;
		}
	return count;
	}

	/////__________________________________  __________________________________/
bool PlayerGoalie::AnyOneWithoutMark()
{
	int attackers[10], attack =0;
	int def;
	//int dangerous[10]
	//find attackers
	for (int i=1; i<11; ++i)
		if ((Mem->OpponentPositionValid(i)) )
			if(	i != Mem->ClosestOpponentToBall())
			if (Mem->InOwnPenaltyArea(Mem->OpponentAbsolutePosition(i)))
				attackers[attack++] =i;
  
    //find dangerous
    for (int i=0; i<attack; ++i)
        if ( (def=Mem->ClosestTeammateTo(Mem->OpponentAbsolutePosition(attackers[i]) ,FALSE))    //!!Valid No Check
			 != Unum_Unknown)		
			if (Mem->TeammateDistanceTo(def, Mem->OpponentAbsolutePosition(attackers[i])) > 3)
				return TRUE;
	return FALSE;
}
/////__________________________________  __________________________________/


bool PlayerGoalie::NoOneInRectangle ()
{
	//~ Rectangle rectangle(Mem->MyPos().x, Mem->BallAbsolutePosition().x,  Mem->MarkerPosition(Flag_RT20).y,
	//~ Mem->MarkerPosition(Flag_RB20).y);                
	//~ for (int i = 2; i <= 11; ++i)
	//~ {
		//~ if (Mem->TeammatePositionValid (i))
		//~ {
			//~ if (Mem->
			    //~ InOwnPenaltyArea (Mem->
					      //~ TeammateAbsolutePosition (i)))
				//~ return FALSE;
		//~ }
	//~ }			//for              
	int	num_opp =0;
	for (int i = 2; i <= 11; ++i)
	{
		if (Mem->OpponentPositionValid (i))
		{
			if (Mem->InOwnPenaltyArea (Mem->OpponentAbsolutePosition (i)) 
				&& Mem-> TeammateDistanceTo(Mem->ClosestTeammateTo(Mem->OpponentAbsolutePosition (i)),Mem->OpponentAbsolutePosition (i)) > 4)
				num_opp++;
		}
	}			//for
	if (num_opp > 1)
		return FALSE;
	return TRUE;
}
//__________________________________    __________________________________/

bool PlayerGoalie::IsOnNimsaz ()
{
	Vector
		F_Flag =
		FurtherMyGoalFlagfromBall ();
	Vector
		N_Flag =
		NearestMyGoalFlagfromBall ();
	Vector
		F_FlagRelPos =
		F_Flag.
		Global2Relative (Mem->BallAbsolutePosition (), 0.0);
	Vector
		N_FlagRelPos =
		N_Flag.
		Global2Relative (Mem->BallAbsolutePosition (), 0.0);
	Vector
		End =
		(F_FlagRelPos +
		 N_FlagRelPos * (F_FlagRelPos.mod () / N_FlagRelPos.mod ())) /
		2;
	End = End.Relative2Global (Mem->BallAbsolutePosition (), 0.0);
	Line
		Nimsaz;
	Nimsaz.LineFromTwoPoints (Mem->BallAbsolutePosition (),Mem->MarkerPosition(Mem->RM_My_Goal));
	if (Nimsaz.dist (Mem->MyPos ()) < .7)
		return TRUE;
	//~ if (Mem->BallDistance () < 8)
		//~ return TRUE;
	return FALSE;
}

//__________________________________ __________________________________//
void PlayerGoalie::GoToNimsazAndLinePrj(Vector point){

	Line Nimsaz;
	Nimsaz.LineFromTwoPoints (point, Mem->MarkerPosition(Mem->RM_My_Goal));
Vector prj =Nimsaz.intersection (GoalieLine);
	 //~ int res = LineCircleIntersect(BallGoalLine, Mem-> SP_goal_width/2, Mem->MarkerPosition(Mem->RM_My_Goal),
			       //~ &psol1, &psol2);//Nimsaz.intersection (GoalieLine);
	
		//~ if(psol1.x > Mem-> MarkerPosition(Mem-> RM_My_Goal).x)
			//~ prj = psol1;
		//~ else
			//~ prj = psol2;
		HLAction.goalie_go_to_point(prj);
	}
//__________________________________ __________________________________//
void
PlayerGoalie::GoToNimsazAndLinePrj ()
{
	Mem->LogAction2(150,"________GOTONIMSAZ");
	OneToOneFlag = FALSE;
	//~ printf("\n______GOTONIMSAZ LINe\n");
	//~ static int counter = 0, fac = 1, xyz;
	Vector F_Flag = FurtherMyGoalFlagfromBall ();
	Vector N_Flag = NearestMyGoalFlagfromBall ();
	Vector F_FlagRelPos =
		F_Flag.Global2Relative (Mem->BallAbsolutePosition (), 0.0);
	Vector N_FlagRelPos =
		N_Flag.Global2Relative (Mem->BallAbsolutePosition (), 0.0);
	Vector End =
		(F_FlagRelPos +
		 N_FlagRelPos * (F_FlagRelPos.mod () / N_FlagRelPos.mod ())) /
		2;
	End = End.Relative2Global (Mem->BallAbsolutePosition (), 0.0);
	Line Nimsaz;
	Nimsaz.LineFromTwoPoints (Mem->BallAbsolutePosition (),Mem->MarkerPosition(Mem->RM_My_Goal));

	//~ Vector E = Mem->MarkerPosition (top);
	//~ Line GoalieLine(1,0,(-1*(E.x+2)));
	//~ Vector prj =Nimsaz.intersection (GoalieLine);
	
	 //~ int res = LineCircleIntersect(BallGoalLine, Mem-> SP_goal_width/2, Mem->MarkerPosition(Mem->RM_My_Goal),
			       //~ &psol1, &psol2);//Nimsaz.intersection (GoalieLine);
		//~ if(psol1.x > Mem-> MarkerPosition(Mem-> RM_My_Goal).x)
			//~ prj = psol1;
		//~ else
			//~ prj = psol2;
	
	//~ float Yrnd = counter * .1 * fac;
	//~ fac *= -1;
	//~ prj.y += -1;
	//~ counter++;
	//~ if (counter > 6)
		//~ counter = 0;
	//~ if(HLAction.face_only_body_to_ball() == AQ_ActionNotQueued|| xyz ) 
		//~ if(HLAction.face_only_body_to_point(Mem->MarkerPosition(Mem -> RM_My_Goal))== AQ_ActionNotQueued )
		
		if(Mem->DistanceTo(BestPosition()) > .6)
			HLAction.goalie_go_to_point (BestPosition(), 1, 100);
		else
			//~ if(fabs(Mem->MyBodyAng()) > 85  && Mem-> MyBodyAng()< 95)
		{
			int factor = 1;
			//~ if(fabs(Mem->BallAngleFromBody())< 90)// MyBodyAng() < 0)	
			if(Mem->BallY() < Mem->MyY())
				factor = -1;
			if(fabs(Mem->MyY() - Mem->BallY()) < 1)
				if(Mem->MyBodyAng() > 0)
					factor = 1;
				else
					factor = -1;
			turn(factor*(90-fabs(Mem->MyBodyAng())));
				//~ change_view (VQ_High, VW_Wide);
		}	//~ TurnForMovingFrontOfGoale();

}
//________________________________________________________________________________________________________________________________________
void PlayerGoalie::ResetCycles(int * cycle_activated){
	
	if(cycle_activated != &Play_On_Cycles )
		Play_On_Cycles = 0;
 	if(cycle_activated != &Goal_Kick_Cycles )
 		Goal_Kick_Cycles  =0 ;
	if(cycle_activated != &Free_Kick_Cycles )
		Free_Kick_Cycles = 0;
	
	}
//________________________________________________________________________________________________________________________________________
bool PlayerGoalie::BallCatchableNextCycle(AngleDeg * ang){
		Vector ut1 = Mem->BallAbsoluteVelocity() ;//+ at;
		Vector point = Mem->BallAbsolutePosition() + ut1;
		  if(Mem->DistanceTo(point) < Mem->SP_catch_area_l  && Mem-> BallSpeed() > .5)  
		  {
			  *ang = Mem->AngleToFromBody(point);	
			  return TRUE;
		  }
		  else
			  return FALSE;
}
//________________________________________________________________________________________________________________________________________



