import React, {useEffect, useState} from 'react';
import {Commit} from "../../interfaces/Commit";
import DiffItemList, {DiffItem} from "./DiffItemList";

type CommitListProps = {
    commits: Commit[]
    handleSelectCommit: (commit: Commit) => void;
}

const CommitList = ({commits, handleSelectCommit}: CommitListProps) => {
    const [selectedIndex, setSelectedIndex] = useState(-1);
    useEffect(() => {
        // Reset selection when commits change
        setSelectedIndex(-1);
    }, [commits])

    const diffItems = commits.map((commit) => {
        let diffItem: DiffItem = {
            id: commit.id,
            createdAt: commit.createdAt,
            authorName: commit.authorName,
            title: commit.title,
        }
        return diffItem;
    });

    const handleSelectDiffItem = (diffItem: DiffItem) => {
        const commit = commits.find((commit) => {
            return commit.id == diffItem.id;
        })
        if (!commit) {
            return;
        }
        handleSelectCommit(commit);
    }

    return (
        <DiffItemList
            diffItems={diffItems}
            diffItemType="Commit"
            handleSelectDiffItem={handleSelectDiffItem}
            selectedIndex={selectedIndex}
            setSelectedIndex={setSelectedIndex}
        />
    );
}

export default CommitList;