import React from 'react';
import {render} from '@testing-library/react';
import Index from '../../pages/project/[serverId]';

describe("Project Folder", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');



    it("Snapshot serverId", () => {
        // Wrap Index with mock router to run snapshot tests
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
        }));
        // @ts-ignore
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
        }));
        render(<Index />);
        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
            isReady: true,
        }));
        render(<Index />);
        expect(mockAxios).toHaveBeenCalledTimes(1);
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
            isReady: false,
        }));
        render(<Index />);
        expect(mockAxios).toHaveBeenCalledTimes(1);
    })

})