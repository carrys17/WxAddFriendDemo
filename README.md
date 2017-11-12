# WxAddFriendDemo


1.0 

寻找添加好友时传入的参数的情况，分析得到每个参数的意义和必要性，最后得出结论。


然后不同的方式有不同的bundle参数，总的来说分为三种


一、搜索方式

1、通过搜索微信号的方式

2、通过搜索手机号的方式

二、群的方式

1、直接在聊天界面中点击别人的头像的方式

2、点击右上角的群信息再点击具体的人添加

三、手机通讯录，也就是在微信中点击添加手机通讯录的方式

一、搜索的方式

搜索微信号的形式

I/set     ( 2625): intent = Intent { cmp=com.tencent.mm/.plugin.profile.ui.ContactInfoUI (has extras) }
I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, Contact_QuanPin=wxidqgpqsx2ozvlp22, Contact_PyInitial=WXIDQGPQSX2OZVLP22, Contact_RegionCode=, Contact_BrandList=<brandlist count="0" ver="601561863"></brandlist>, Contact_KSnsBgId=0, add_more_friend_search_scene=2, Contact_KWeiboNick=null, Contact_KSnsBgUrl=null, Contact_KSnsIFlag=0, Contact_KWeibo_flag=0, Contact_Signature=null, Contact_BIZ_KF_WORKER_ID=null, Contact_VUser_Info=null, Contact_KHideExpose=true, Contact_Nick=慧雯, Contact_User=v1_2c36ea4042bb6e382de53f1966494a8fc1bb9ebac4ae1eb02b1b346233b27b3dc98d9b4466a947552462be8d5c0111b8@stranger, Contact_VUser_Info_Flag=0, Contact_Sex=0, Contact_Alias=sdq13540497060, Contact_Scene=3, Contact_KWeibo=null, Contact_NeedShowChangeSnsPreButton=false}]

搜索手机号的形式

I/set     ( 2625): intent = Intent { cmp=com.tencent.mm/.plugin.profile.ui.ContactInfoUI (has extras) }
I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, Contact_QuanPin=wxidpkspt5ckkejk12, Contact_PyInitial=WXIDPKSPT5CKKEJK12, Contact_RegionCode=, Contact_BrandList=<brandlist count="0" ver="668298634"></brandlist>, Contact_KSnsBgId=0, add_more_friend_search_scene=2, Contact_Search_Mobile=18814143558, Contact_KWeiboNick=null, Contact_KSnsBgUrl=null, Contact_KSnsIFlag=0, Contact_KWeibo_flag=0, Contact_Signature=null, Contact_BIZ_KF_WORKER_ID=null, Contact_VUser_Info=null, Contact_KHideExpose=true, Contact_Nick=趁你我还年轻, Contact_User=v1_6f37fdf0632e975154dcb187f1408fe15feee4047fe4d313de611496f1074ddcb64016d8947ab158ab6e9af86a134daf@stranger, Contact_VUser_Info_Flag=0, Contact_Sex=0, Contact_Alias=FJX613558, Contact_Scene=15, Contact_KWeibo=null, Contact_NeedShowChangeSnsPreButton=false}]

二、群里非好友时

直接在聊天界面点击头像
I/set     ( 2625): intent = Intent { cmp=com.tencent.mm/.plugin.profile.ui.ContactInfoUI (has extras) }
I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, room_name=636235605@chatroom, Is_RoomOwner=false, Contact_RoomNickname=吃瓜的鸿, Contact_User=wxid_n24mdcaqj37s22, Contact_Scene=14, Contact_NeedShowChangeSnsPreButton=false}]


先点击右上角显示群信息，再点击头像
I/set     ( 2625): intent = Intent { cmp=com.tencent.mm/.plugin.profile.ui.ContactInfoUI (has extras) }
I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, Contact_Mobile_MD5=, Contact_ChatRoomId=636235605@chatroom, room_name=636235605@chatroom, Is_RoomOwner=false, Contact_RoomNickname=蔡建, Contact_RoomMember=true, Contact_Nick=林, Contact_User=wxid_lnt4ahvg2esr22, Contact_Scene=14, Contact_RemarkName=, Contact_NeedShowChangeSnsPreButton=false}]

三、通讯录方式
I/set     ( 2625): intent = Intent { cmp=com.tencent.mm/.plugin.profile.ui.ContactInfoUI (has extras) }
I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, Contact_Mobile_MD5=270bae1b55a890759987f9047b81c027, Contact_RegionCode=CN_Guangdong_Dongguan, Contact_ShowUserName=false, Contact_Signature= boeey, Contact_Nick=桌球, Contact_User=v1_f2beaa1797ef0252d0421242b04ab14613f59c18c49bab2e395fc26badaf517a@stranger, Contact_Sex=1, Contact_Alias=, Contact_Scene=13, Contact_NeedShowChangeSnsPreButton=false}]


