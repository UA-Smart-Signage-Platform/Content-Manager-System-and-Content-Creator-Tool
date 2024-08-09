import  Logo  from '../../static/green-name.svg?react';
import React, { useCallback, useState } from 'react';

function SignInDiv({setNyan}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loginRoot,setLoginRoot] = useState(false);
    const [clickTimes,setClickTimes] = useState(0);

    const clickTimeCheck = useCallback(()=>{
        if(clickTimes < 9){
            setClickTimes(clickTimes+1)
        }else if (clickTimes === 9){
            setNyan(true);
            setClickTimes(11);
        }

    },[clickTimes])

    return(
        <div className='flex flex-col gap-20 w-[50%] mx-auto items-center z-40 pt-[10%] relative'>
          <div onClick={()=>clickTimeCheck()}><Logo></Logo></div>
          {loginRoot ? 
          <div className='flex flex-col gap-6 items-center justify-center'>
            <div className='w-full'>
              <p className=' justify-start w-full text-2xl text-[#95A967]'>Sign in as Root</p>
              <input className=' border-secondary border-2 rounded-md font-bold p-1 text-xl bg-secondaryLight'
                    placeholder='password'
              />
            </div>
            <button className='bg-primary font-bold text-lg p-1 rounded-md w-full'>Sign In</button>
            <button className='bg-secondary font-bold text-lg p-1 rounded-md w-[75%]'
                    onMouseDown={()=> setLoginRoot(!loginRoot)}>
                    Back
            </button>
          </div>
          : 
          <div className='flex flex-col gap-6 items-center justify-center'>
            <button className='bg-primary font-bold text-xl p-3 rounded-md px-16'>Sign in with IDP</button>
            <button className='bg-[#95A967] font-bold text-lg p-1 rounded-md w-[75%]'
                    onMouseDown={()=> setLoginRoot(!loginRoot)}>
                    Admin Sign in
            </button>
          </div>
          }
        </div>
    )
}

export default SignInDiv;