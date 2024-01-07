import React, { createContext, useState, useMemo, ReactNode } from 'react';

interface MenuContextProps {
  activeMenu: string;
  setActiveMenu: React.Dispatch<React.SetStateAction<string>>;
}

interface MenuInvitationCodeContextProps {
  invitationCode: string;
  setInvitationCode: React.Dispatch<React.SetStateAction<string>>;
}

interface CourseOwnerIdContextProps {
  courseOwnerId: string;
  setCourseOwnerId: React.Dispatch<React.SetStateAction<string>>;
}

interface MenuProviderProps {
  children: ReactNode;
}

export const MenuContext = createContext<MenuContextProps>({
  activeMenu: 'stream',
  setActiveMenu() {},
});

export const MenuProvider: React.FC<MenuProviderProps> = ({ children }) => {
  const [activeMenu, setActiveMenu] = useState<string>('');

  const value = useMemo(() => {
    return {
      activeMenu,
      setActiveMenu,
    };
  }, [activeMenu]);

  return <MenuContext.Provider value={value}>{children}</MenuContext.Provider>;
};

export const InvitationCodeContext = createContext<MenuInvitationCodeContextProps>({
  invitationCode: '',
  setInvitationCode() {},
});

export const InvitationCodeProvider: React.FC<MenuProviderProps> = ({ children }) => {
  const [invitationCode, setInvitationCode] = useState<string>('');

  const value = useMemo(() => {
    return {
      invitationCode,
      setInvitationCode,
    };
  }, [invitationCode]);

  return <InvitationCodeContext.Provider value={value}>{children}</InvitationCodeContext.Provider>;
};

export const CourseOwnerIdContext = createContext<CourseOwnerIdContextProps>({
  courseOwnerId: '',
  setCourseOwnerId() {},
});

export const CourseOwnerIdProvider: React.FC<MenuProviderProps> = ({ children }) => {
  const [courseOwnerId, setCourseOwnerId] = useState<string>('');

  const value = useMemo(() => {
    return {
      courseOwnerId,
      setCourseOwnerId,
    };
  }, [courseOwnerId]);

  return <CourseOwnerIdContext.Provider value={value}>{children}</CourseOwnerIdContext.Provider>;
};
