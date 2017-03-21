# AudioDragButton
This button just like telegram audio record button.
You can set slide width & scala.
Notice: if you want to use in scroll view, you must rewirte scroll view onInterceptTouchEvent. 
When return false, audio drag button will not call onTouchEvent MotionEvent.ACTION_CANCEL.
  
  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if(!mIsIntercept) {
      return false;
    }else{
      return super.onInterceptTouchEvent(ev);
    }
  }
