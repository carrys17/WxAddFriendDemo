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


2.0

通过前面总结的规律，实现自动添加好友的效果。

在ui布局中，要求可以选择一个txt文本。里面有要添加的微信id的值。每行一个，实现自动遍历该文本中的微信id，添加相应的好友。

界面可以选择用户想要的添加场景，每次添加的时间间隔，用户想要填入的验证信息，当前添加的人数。

具体逻辑看代码，注释很清楚了。

3.0 

新增两种三种添加场景，分别为搜索QQ号，名片，二维码的方式，它们的场景号为12,17,30.

名片的添加会有新的字段v2值，看起来像是用于反垃圾的票据，而且添加到同一个人3次会报出加人频繁了，还有就是如果对方设置了不能通过名片获取到他的信息，也是无法添加的。

二维码的也有v2值，其他没啥限制。

看更新的ManiActivity.class 


修改添加人的逻辑实现

遇到的问题：

首先就是更改查找的方式，之前是通过id的方式，这种每次微信一更新就会改变id，所以要用一种通用的方式来实现。通过查找页面中的文本来获取控件。
递归查找当前页面是否有输入框/返回键，要设置一个临时变量，不然递归返回的是个参数，所以每次都为null，而且记得找到就return ，而不是刚开始改进的那种break的方式。

然后就是线程通过问题了，因为要在activity中获取到辅助功能类的服务，而且在界面变化时执行相应的操作，这就需要在activity和service之间共享变量了。每次获取前睡眠一段时间，直到获取到最新的值为止。有一个需要注意的地方就是Android的activity和service都是在主线程的，所以在activity中开启子线程执行操作，这样睡眠时让出cpu给主线程执行，实现同步。

最后就是吐司的问题了，因为前面提到，activity中的操作都是在子线程执行的，所以在吐司时就是在子线程，然后这里的子线程的吐司有很大的几率被微信自己的覆盖了，所以吐司时通过handler来实现在主线程操作，这样吐司才能正常显示。

其他的就是一些小问题了，现在的代码在本地测试是没问题的，但是如果cpu占用高的情况下，就会出现一些奇怪的问题，所以还需要再次改进。

4.0 

添加时自动跳转界面的调整。

之前的如果cpu占用高或者网速不好的话，可能获取不到控件，所以现在是利用 do  while的方式来不断的获取，当然也有一个超时限制，然后在每个操作之间的间隔调高，增加稳定性，加快删除原本的验证信息的速率。这样的稳定性就非常好了。
然后还有考虑到特殊情况的处理，比如对方设置了某种场景你不能添加到他的时候，此时就应该点击确定后再点击返回到主界面。


今天最后的版本就是在ui界面中添加了一个可以设置添加时bundle参数填入的多少选择，有三种情况，后面的两种有三种场景可用，第一种情况没有限制。

每次操作间的时间间隔也来了个随机生成的，在1500到2000毫秒之间。

5.0 

新增直接跳转到验证界面填入验证信息加人，而不是到用户界面再点击添加的方式。这种需要注意的就是通过intent的方式来跳转的，不是之前的adb的方式。所以需要用到微信的context，需要hook来拿到，然后又是进程间通信的问题。这里不能用之前的SharedPrefrences结合ContentProvider的方式来通信，因为如果是这种方式的话，会在hook中做跳转界面的操作。但是hook又是一个不断的过程，所以会卡顿到微信的打开。换一个思路，采用BroadcastReceiver的方式，通过在activity发送广播，在xposed中注册广播，然后将获取到的微信Context传给广播接收者，在onReceiver（） 中去执行界面的跳转操作。

